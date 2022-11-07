package org.ton.proxy.rldp

import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
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
    private var totalSize by atomic(0L)
    private var part by atomic(0)
    private var decoder = atomic<RaptorQFecDecoder?>(null)
    private var processedSize by atomic(0L)
    private var confirmCount by atomic(0)
    private val messagesChannel = Channel<RldpMessagePartData>(Channel.UNLIMITED)

    override fun transferPackets(): Flow<RldpMessagePart> = flow {
        for (message in messagesChannel) {
            val response = processPart(message)
            if (response != null) {
                emit(response)
            }
        }
    }

    override fun receivePart(message: RldpMessagePartData) {
        messagesChannel.trySend(message)
    }

    private suspend fun processPart(
        message: RldpMessagePartData
    ): RldpMessagePart? {
        val fecType = requireNotNull(message.fec_type as? FecRaptorQ) { "Unsupported FEC type: ${message.fec_type}" }
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
            0 -> initDecoder(fecType)
            -1 -> return RldpComplete(id, message.part)
            else -> return null
        }

        // Decode message data
        val data = decoder.decode(message.seqno, message.data)
        if (data != null) {
            processedSize += data.size

            if (processedSize < totalSize) {
                this.decoder.value = null
                part += 1
                confirmCount = 0
            }

            byteChannel.writeFully(data)
            if (processedSize == totalSize) {
                messagesChannel.close()
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

    private fun initDecoder(fecType: FecRaptorQ) = decoder.updateAndGet {
        it?.also {
            require(it.fecType == fecType) { "FEC type mismatch" }
        } ?: RaptorQFecDecoder(fecType)
    }!!
}
