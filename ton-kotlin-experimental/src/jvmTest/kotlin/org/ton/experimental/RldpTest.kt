package org.ton.experimental

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import org.ton.proxy.rldp.RldpInputTransfer
import org.ton.proxy.rldp.RldpOutputTransfer
import org.ton.proxy.rldp.fec.RaptorQFecEncoder
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class RldpTest {
    @OptIn(ExperimentalTime::class)
    @Test
    fun test() = runBlocking {
        val output = newSingleThreadContext("RLDP output")
        val input = newSingleThreadContext("RLDP input")

        repeat(1000) {
            val originalData = Random.nextBytes(1024 * 4)

            val outputTransfer = RldpOutputTransfer.of(originalData)
            val inputTransfer = RldpInputTransfer.of(outputTransfer.id)

            launch(output) {
                outputTransfer.transferPackets().buffer().collect {
                    inputTransfer.receivePart(it)
                }
            }
            launch(input) {
                inputTransfer.transferPackets().collect {
                    outputTransfer.receivePart(it)
                }
            }

            val (transferred, time) = measureTimedValue {
                inputTransfer.byteChannel.readRemaining().readBytes()
            }

            assertContentEquals(originalData, transferred)
            val speed = transferred.size * 8.0 / time.toDouble(DurationUnit.SECONDS) / 1024 / 1024

            println("$it Speed: $speed MBit/s")
        }
    }

    @Test
    fun raptorQFec() = runBlocking {
        val data = Random.nextBytes(1024 * 20)
        val encoder = RaptorQFecEncoder(data)
        println(encoder.fecType)
        val symbols = Array(encoder.fecType.symbol_count) {
            val array = ByteArray(encoder.fecType.symbol_size)
            val seqno = encoder.encode(it, array)
            seqno to array
        }
    }
}
