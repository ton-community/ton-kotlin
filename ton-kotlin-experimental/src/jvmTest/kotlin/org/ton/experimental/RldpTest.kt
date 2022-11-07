package org.ton.experimental

import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.ton.crypto.SecureRandom
import org.ton.proxy.rldp.RldpInputTransfer
import org.ton.proxy.rldp.RldpOutputTransfer
import org.ton.proxy.rldp.fec.RaptorQFecEncoder
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class RldpTest {
    @OptIn(ExperimentalTime::class)
    @Test
    fun test() = runBlocking {
        repeat(100) {
            val originalData = Random.nextBytes(1024 * 1024)

            val outputTransfer = RldpOutputTransfer.of(originalData)
            val inputTransfer = RldpInputTransfer.of(outputTransfer.id)

            GlobalScope.launch {
                outputTransfer.transferPackets().buffer(2).collect {
                    val result = inputTransfer.receivePart(it)
                    if (result != null) {
                        outputTransfer.receivePart(result)
                    }
                }
            }

            val (transferred, time) = measureTimedValue {
                inputTransfer.byteChannel.readRemaining().readBytes()
            }

            println("original   : ${originalData.size}")
            println("transferred: ${transferred.size}")
            println("time       : $time")
            // Calculate megabits per sec
            println("speed      : ${transferred.size * 8.0 / time.toDouble(DurationUnit.SECONDS) / 1024 / 1024} MBits/sec")
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
