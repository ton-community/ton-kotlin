package org.ton.adnl

import kotlinx.atomicfu.atomic
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.*
import org.ton.crypto.digest.sha256
import org.ton.tl.ByteString
import org.ton.tl.asByteString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public interface AdnlPeerSession {
    public val adnl: Adnl
    public val hugePacketMaxSize: Int get() = adnl.hugePacketMaxSize + 128
    public val mtu: Int get() = adnl.mtu + 128

    public suspend fun sendMessages(
        vararg messages: AdnlMessage
    )

    public suspend fun sendQuery(
        data: ByteString,
        timeout: Duration = 15.seconds
    ): ByteString

    public fun handlePacket(packet: AdnlPacketContents)

    public fun handleMessage(message: AdnlMessage): Unit = when (message) {
        is AdnlMessageAnswer -> handleMessage(message)
        is AdnlMessageConfirmChannel -> handleMessage(message)
        is AdnlMessageCreateChannel -> handleMessage(message)
        is AdnlMessageCustom -> handleMessage(message)
        is AdnlMessageNop -> handleMessage(message)
        is AdnlMessagePart -> handleMessage(message)
        is AdnlMessageQuery -> handleMessage(message)
        is AdnlMessageReinit -> handleMessage(message)
    }

    public fun handleMessage(message: AdnlMessageCreateChannel)
    public fun handleMessage(message: AdnlMessageConfirmChannel)
    public fun handleMessage(message: AdnlMessageCustom)
    public fun handleMessage(message: AdnlMessageNop) {}
    public fun handleMessage(message: AdnlMessageReinit)
    public fun handleMessage(message: AdnlMessageQuery)
    public fun handleMessage(message: AdnlMessageAnswer)
    public fun handleMessage(message: AdnlMessagePart)
}


public interface AdnlPeer {
    public suspend fun sendMessages(
        src: AdnlIdShort,
        vararg messages: AdnlMessage
    )

    public suspend fun sendQuery(
        src: AdnlIdShort,
        data: ByteString,
        timeout: Duration = 15.seconds
    ): ByteString

    public fun handlePacket(dest: AdnlIdShort, packet: AdnlPacketContents)
}

internal open class AdnlPeerSessionImpl(
    override val adnl: Adnl
) : AdnlPeerSession {
    private var hugeMessageHash: ByteString = ZERO_HASH
    private var hugeMessage: ByteArray = EMPTY_BYTES
    private val hugeMessageOffset = atomic(0)

    override suspend fun sendMessages(vararg messages: AdnlMessage) {
        val newMessages = ArrayList<AdnlMessage>()
        messages.forEach {  message ->
            if (AdnlMessage.sizeOf(message) <= mtu) {
                newMessages.add(message)
            } else {
                val b = AdnlMessage.encodeToByteArray(message, boxed = true)
                check(b.size <= hugePacketMaxSize)
                val hash = sha256(b).asByteString()
                val size = b.size
                var offset = 0
                val partSize = adnl.mtu
                while (offset < size) {
                    val data = b.copyOfRange(offset, minOf(offset + partSize, b.size))
                    val partMessage = AdnlMessagePart(
                        hash,
                        size,
                        offset,
                        data.asByteString()
                    )
                    newMessages.add(partMessage)
                    offset += partSize
                }
            }
        }
        sendRawMessages(newMessages)
    }

    override suspend fun sendQuery(data: ByteString, timeout: Duration): ByteString {
        TODO("Not yet implemented")
    }

    override fun handlePacket(packet: AdnlPacketContents) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessageCreateChannel) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessageConfirmChannel) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessageCustom) {
        println("Custom: ${message.data}")
    }

    override fun handleMessage(message: AdnlMessageReinit) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessageQuery) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessageAnswer) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: AdnlMessagePart) {
        check(message.totalSize <= hugePacketMaxSize) { "too big message: size=${message.totalSize}" }
        check(message.hash == ZERO_HASH) { "zero hash" }
        if (message.hash != hugeMessageHash) {
            hugeMessageHash = if (message.offset == 0) message.hash else ZERO_HASH
            hugeMessage = ByteArray(message.totalSize)
        }
        check(message.totalSize == hugeMessage.size) { "invalid size" }
        check(message.data.size + message.offset <= message.totalSize) { "bad part" }

        if (message.offset == hugeMessageOffset.value) {
            message.data.copyInto(hugeMessage, message.offset)
            val totalSize = hugeMessageOffset.addAndGet(message.data.size)

            if (totalSize == hugeMessage.size) {
                val actualMessageHash = sha256(hugeMessage).asByteString()
                check(actualMessageHash == hugeMessageHash) {
                    "hash mismatch, expected: $hugeMessageHash, actual: $actualMessageHash"
                }
                hugeMessageHash = ZERO_HASH
                hugeMessageOffset.value = 0
                hugeMessage = EMPTY_BYTES
                val recoveredMessage = AdnlMessage.decodeBoxed(hugeMessage)
                handleMessage(recoveredMessage)
            }
        }
    }

    private suspend fun sendRawMessages(messages: List<AdnlMessage>) {
        adnl
    }

    companion object {
        private val ZERO_HASH = ByteArray(32).asByteString()
        private val EMPTY_BYTES = ByteArray(0)
    }
}
