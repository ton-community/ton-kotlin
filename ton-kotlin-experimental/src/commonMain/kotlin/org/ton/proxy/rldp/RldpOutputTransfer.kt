package org.ton.proxy.rldp

import io.ktor.utils.io.core.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.ton.api.rldp.RldpComplete
import org.ton.api.rldp.RldpMessagePart
import org.ton.api.rldp.RldpMessagePartData
import org.ton.bitstring.BitString
import org.ton.proxy.rldp.fec.RaptorQFecEncoder
import kotlin.random.Random

interface RldpOutputTransfer {
    val id: BitString
    val totalSize: Long

    fun transferPackets(): Flow<RldpMessagePart>

    fun progress(): Flow<Double>

    fun receivePart(message: RldpMessagePart)

    companion object {
        @JvmStatic
        fun of(
            data: ByteArray,
            id: BitString = BitString(Random.nextBytes(32))
        ): RldpOutputTransfer = RldOutputTransferImpl(data.size.toLong(), ByteReadPacket(data), id)

        @JvmStatic
        fun of(
            totalSize: Long,
            input: Input,
            id: BitString = BitString(Random.nextBytes(32)),
        ): RldpOutputTransfer = RldOutputTransferImpl(totalSize, input, id)
    }
}

private class RldOutputTransferImpl(
    override val totalSize: Long,
    private val input: Input,
    override val id: BitString,
) : RldpOutputTransfer {
    var part = 0

    private val ackParts = Channel<RldpMessagePart>(Channel.UNLIMITED)

    override fun transferPackets(): Flow<RldpMessagePart> = flow {
        while (currentCoroutineContext().isActive) {
            val bytes = input.readBytesOf(max = SLICE_SIZE)
            if (bytes.isEmpty()) {
                break
            }
            val encoder = RaptorQFecEncoder(bytes)
            part@ for (i in 0 until encoder.fecType.symbol_count) {
                while (currentCoroutineContext().isActive) {
                    val ackMessage = ackParts.tryReceive().getOrNull() ?: break
                    if (ackMessage is RldpComplete && ackMessage.part == part) {
                        break@part
                    }
                }
                val symbol = ByteArray(encoder.fecType.symbol_size)
                val seqno = encoder.encode(i, symbol)
                val packet = RldpMessagePartData(
                    transfer_id = id,
                    part = part,
                    seqno = seqno,
                    fec_type = encoder.fecType,
                    data = symbol,
                    total_size = totalSize,
                )
                emit(packet)
            }
            part++
        }
    }

    override fun receivePart(message: RldpMessagePart) {
        ackParts.trySend(message)
    }

    override fun progress(): Flow<Double> {
        TODO("Not yet implemented")
    }


    override fun toString(): String = "RldpOutputTransfer(id=$id, totalSize=$totalSize)"

    companion object {
        const val SLICE_SIZE = 2_000_000
        const val SYMBOL_SIZE = 768
        const val WINDOW_SIZE = 1000
        const val WAVES_INTERVAL_MILLISECONDS = 10L
    }
}
