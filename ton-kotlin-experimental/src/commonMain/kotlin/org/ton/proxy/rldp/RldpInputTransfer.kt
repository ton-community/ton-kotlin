package org.ton.proxy.rldp

import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ton.api.fec.FecRaptorQ
import org.ton.api.rldp.RldpComplete
import org.ton.api.rldp.RldpConfirm
import org.ton.api.rldp.RldpMessagePart
import org.ton.api.rldp.RldpMessagePartData
import org.ton.bitstring.BitString
import org.ton.proxy.rldp.fec.RaptorQFecDecoder

interface RldpInputTransfer {
    val id: BitString
    val byteChannel: ByteReadChannel

    fun receivePart(message: RldpMessagePart) = when (message) {
        is RldpMessagePartData -> receivePart(message)
        else -> throw IllegalArgumentException("Unsupported message type: ${message::class}")
    }

    fun receivePart(message: RldpMessagePartData)

    fun transferPackets(): Flow<RldpMessagePart>

    companion object {
        @JvmStatic
        fun of(
            id: BitString
        ): RldpInputTransfer = RldpInputTransferImpl(id)
    }
}

private class RldpInputTransferImpl(
    override val id: BitString,
    override val byteChannel: ByteChannel = ByteChannel(),
) : RldpInputTransfer {
    private var decoder = atomic<RaptorQFecDecoder?>(null)
    private val messagesChannel = Channel<RldpMessagePartData>(Channel.UNLIMITED)

    override fun transferPackets(): Flow<RldpMessagePart> = flow {
        var decoder: RaptorQFecDecoder? = null
        var totalSize = -1L
        var part = 0
        var processed = -2L
        try {
            while (processed < totalSize) {
                val message = messagesChannel.receive()
                if (message.part != part) {
                    continue
                }

                if (totalSize == -1L) {
                    totalSize = message.total_size
                    processed = 0
                } else {
                    require(totalSize == message.total_size) { "Total size mismatch, expected: $totalSize, actual: ${message.total_size}" }
                }

                var currentDecoder = decoder
                if (currentDecoder == null) {
                    currentDecoder = RaptorQFecDecoder(message.fec_type as FecRaptorQ)
                    decoder = currentDecoder
                } else {
                    require(currentDecoder.fecType == message.fec_type) { "Fec type mismatch, expected: ${currentDecoder.fecType}, actual: ${message.fec_type}" }
                }

                val result = currentDecoder.decode(message.seqno, message.data)
                if (result != null) {
                    val confirm = RldpConfirm(id, part, message.seqno)
                    emit(confirm)
                    byteChannel.writeFully(result)
                    val complete = RldpComplete(id, part)
                    emit(complete)
                    part++
                    processed += result.size
                    decoder = null
                } else {
                    val confirm = RldpConfirm(id, part, message.seqno)
                    emit(confirm)
                }
            }
        } finally {
            byteChannel.close()
            messagesChannel.close()
        }
    }

    override fun receivePart(message: RldpMessagePartData) {
        messagesChannel.trySend(message)
    }
}
