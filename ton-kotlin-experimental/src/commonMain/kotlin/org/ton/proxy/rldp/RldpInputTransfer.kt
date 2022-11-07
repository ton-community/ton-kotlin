package org.ton.proxy.rldp

import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import org.ton.api.fec.FecRaptorQ
import org.ton.api.rldp.RldpComplete
import org.ton.api.rldp.RldpConfirm
import org.ton.api.rldp.RldpMessagePart
import org.ton.api.rldp.RldpMessagePartData
import org.ton.bitstring.BitString
import org.ton.proxy.rldp.fec.RaptorQFecDecoder

interface RldpInputTransfer {
    val byteChannel: ByteReadChannel

    suspend fun receivePart(message: RldpMessagePartData): RldpMessagePart?

    companion object {
        @JvmStatic
        fun of(
            id: BitString
        ): RldpInputTransfer = RldpInputTransferImpl(id)
    }
}

private class RldpInputTransferImpl(
    private val id: BitString,
    override val byteChannel: ByteChannel = ByteChannel(),
) : RldpInputTransfer {
    private var totalSize by atomic(0L)
    private var part by atomic(0)
    private var decoder by atomic<RaptorQFecDecoder?>(null)
    private var processedSize by atomic(0L)
    private var confirmCount by atomic(0)

    override suspend fun receivePart(
        message: RldpMessagePartData
    ): RldpMessagePart? {
        val fecType = message.fec_type
        require(fecType is FecRaptorQ) { "Only RaptorQ is supported" }

        // Initialize `total_size` on first message
        val totalSize = when (val currentTotalSize = totalSize) {
            0L -> {
                totalSize = message.total_size
                message.total_size
            }

            message.total_size -> message.total_size
            else -> throw IllegalStateException("Total size mismatch: $currentTotalSize != ${message.total_size}")
        }

        // Check message part
        val decoder = when (message.part.compareTo(part)) {
            0 -> {
                when (val currentDecoder = decoder) {
                    null -> {
                        val newDecoder = RaptorQFecDecoder(fecType)
                        decoder = newDecoder
                        newDecoder
                    }

                    else -> {
                        require(currentDecoder.fecType == fecType) { "FEC type mismatch" }
                        currentDecoder
                    }
                }
            }

            -1 -> {
                return RldpComplete(id, message.part)
            }

            else -> return null
        }

        // Decode message data
        decoder.decode(message.seqno, message.data)?.let { data ->
            processedSize += data.size

            if (processedSize < totalSize) {
                this.decoder = null
                part += 1
                confirmCount = 0
            }

            byteChannel.writeFully(data)
            if (processedSize == totalSize) {
                byteChannel.close()
            }

            return RldpComplete(id, message.part)
        }
        if (confirmCount >= 9) {
            confirmCount = 0
            return RldpConfirm(id, message.part, decoder.seqno)
        }
        confirmCount += 1
        return null
    }
}
