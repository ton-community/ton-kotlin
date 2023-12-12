package org.ton.adnl

import org.ton.adnl.peer.AdnlMessagePartDecoder
import org.ton.adnl.peer.AdnlMessagePartEncoder
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.*
import org.ton.tl.ByteString
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
    private val messagePartDecoder = AdnlMessagePartDecoder(hugePacketMaxSize)
    private val messagePartEncoder = AdnlMessagePartEncoder(mtu, hugePacketMaxSize)

    override suspend fun sendMessages(vararg messages: AdnlMessage) {
        val newMessages = ArrayList<AdnlMessage>()
        messages.forEach { message ->
            if (AdnlMessage.sizeOf(message) <= mtu) {
                newMessages.add(message)
            } else {
                newMessages.addAll(messagePartEncoder.encode(message))
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
        val decodedMessage = messagePartDecoder.decode(message)
        if (decodedMessage != null) {
            handleMessage(decodedMessage)
        }
    }

    private suspend fun sendRawMessages(messages: List<AdnlMessage>) {
        adnl
    }
}
