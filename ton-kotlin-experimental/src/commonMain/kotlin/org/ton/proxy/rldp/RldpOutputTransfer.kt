package org.ton.proxy.rldp

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import org.ton.api.rldp.RldpComplete
import org.ton.api.rldp.RldpConfirm
import org.ton.api.rldp.RldpMessagePart
import org.ton.api.rldp.RldpMessagePartData
import org.ton.bitstring.BitString
import org.ton.proxy.rldp.fec.RaptorQFecEncoder
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

interface RldpOutputTransfer {
    val id: BitString

    fun transferPackets(): Flow<RldpMessagePartData>

    fun confirm(message: RldpConfirm)

    fun complete(part: Int)

    fun receivePart(message: RldpMessagePart) {
        when (message) {
            is RldpComplete -> complete(message.part)
            is RldpConfirm -> confirm(message)
            else -> throw IllegalArgumentException("Unsupported message type: ${message::class}")
        }
    }

    companion object {
        @JvmStatic
        fun of(
            data: ByteArray,
            id: BitString = BitString(Random.nextBytes(32))
        ): RldpOutputTransfer = RldOutputTransferImpl(data.size.toLong(), ByteReadChannel(data), id)

        @JvmStatic
        fun of(
            totalSize: Long,
            channel: ByteReadChannel,
            id: BitString = BitString(Random.nextBytes(32)),
        ): RldpOutputTransfer = RldOutputTransferImpl(totalSize, channel, id)
    }
}

private class RldOutputTransferImpl(
    private val totalSize: Long,
    private val byteReadChannel: ByteReadChannel,
    override val id: BitString,
) : RldpOutputTransfer {
    private lateinit var encoder: RaptorQFecEncoder
    private var _part = atomic(0)
    private var part
        get() = _part.value
        set(value) {
            _part.compareAndSet(value - 1, value)
        }
    private var seqnoOut by atomic(0)
    private var seqnoIn by atomic(0)
    private var hasReply by atomic(false)
    private var responseChannel = Channel<Unit>()

    override fun transferPackets() = flow {
        transfer@ while (currentCoroutineContext().isActive) {
            var packetCount = startNextPart()
            if (packetCount <= 0) break
            val part = part
            part@ while (currentCoroutineContext().isActive && packetCount > 0) {
                val waves = min(packetCount, 10)
                for (wave in 0 until waves) {
                    emit(preparePart())
                    packetCount--
                    if (isFinishedOrNext(part)) {
                        break@part
                    }
                }
                withTimeoutOrNull(1000) {
                    responseChannel.receive()
                }
                if (isFinishedOrNext(part)) {
                    break@part
                }
            }
        }
    }

    override fun confirm(message: RldpConfirm) {
        if (part == this.part) {
            if (message.seqno in seqnoIn..seqnoOut) {
                seqnoIn = message.seqno
                responseChannel.trySend(Unit)
            }
        }
    }

    override fun complete(part: Int) {
        if (part == this.part) {
            this.part++
            responseChannel.trySend(Unit)
        }
    }

    private suspend fun startNextPart(): Int {
        if (isFinished()) return -1
        val part = this.part
        val processed = part * SLICE_SIZE
        if (processed >= totalSize) return -1

        val partSize = minOf(SLICE_SIZE, totalSize - processed)
        val data = byteReadChannel.readRemaining(partSize).readBytes()
        encoder = RaptorQFecEncoder(data)
        seqnoOut = 0
        seqnoIn = 0
        return encoder.fecType.symbol_count
    }

    fun preparePart(): RldpMessagePartData {
        val encoder = encoder

        var seqnoOut = seqnoOut
        val previousSeqnoOut = seqnoOut

        val data = ByteArray(encoder.fecType.symbol_size)
        seqnoOut = encoder.encode(seqnoOut, data)

        val seqnoIn = seqnoIn
        var nextSeqnoOut = seqnoOut
        if (seqnoOut - seqnoIn <= WINDOW_SIZE) {
            if (previousSeqnoOut == seqnoOut) {
                nextSeqnoOut += 1
            }
            this.seqnoOut = nextSeqnoOut
        }

        return RldpMessagePartData(
            transfer_id = id,
            fec_type = encoder.fecType,
            part = part,
            total_size = totalSize,
            seqno = seqnoOut,
            data = data
        )
    }

    private fun isFinished() = hasReply && (part + 1) * SLICE_SIZE >= totalSize
    private fun isFinishedOrNext(part: Int): Boolean {
        val isFinished = isFinished()
        if (isFinished) {
            return true
        }
        return when (this.part) {
            part -> false
            part + 1 -> true
            else -> throw IllegalStateException("Unexpected part $part")
        }
    }

    companion object {
        const val SLICE_SIZE = 2_000_000L
        const val SYMBOL_SIZE = 768
        const val WINDOW_SIZE = 1000
        const val WAVES_INTERVAL_MILLISECONDS = 10L
    }
}
