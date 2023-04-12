package org.ton.adnl

import org.ton.adnl.engine.AdnlNetworkEngine
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.ByteString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public interface Adnl {
    public val hugePacketMaxSize: Int get() = 1024 * 8
    public val mtu: Int get() = 1024
    public val networkEngine: AdnlNetworkEngine

    public fun addLocalId(privateKey: PrivateKey)

    public suspend fun sendMessage(
        src: AdnlIdShort,
        dest: AdnlIdShort,
        data: ByteString
    )

    public suspend fun sendQuery(
        src: AdnlIdShort,
        dest: AdnlIdShort,
        data: ByteString,
        timeout: Duration = 15.seconds,
        maxAnswerSize: Int = Int.MAX_VALUE
    )

    public fun addPeer(localId: AdnlIdShort, remoteKey: PublicKey, addressList: AdnlAddressList)

    public fun subscribeMessage(
        dest: AdnlIdShort,
        handler: MessageHandler
    ): MessageHandler

    public fun subscribeQuery(
        dest: AdnlIdShort,
        handler: QueryHandler
    ): QueryHandler

    public fun interface MessageHandler {
        public fun onMessage(src: AdnlIdShort, dest: AdnlIdShort, message: ByteString)
    }

    public fun interface QueryHandler {
        public fun onQuery(src: AdnlIdShort, dest: AdnlIdShort, query: ByteString): ByteString
    }

    public companion object {
        public fun create(
            networkEngine: AdnlNetworkEngine
        ): Adnl = AdnlImpl(networkEngine)
    }
}

private class AdnlImpl(
    override val networkEngine: AdnlNetworkEngine
) : Adnl {
    override fun addLocalId(privateKey: PrivateKey) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(src: AdnlIdShort, dest: AdnlIdShort, data: ByteString) {
        TODO("Not yet implemented")
    }

    override suspend fun sendQuery(
        src: AdnlIdShort,
        dest: AdnlIdShort,
        data: ByteString,
        timeout: Duration,
        maxAnswerSize: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun addPeer(localId: AdnlIdShort, remoteKey: PublicKey, addressList: AdnlAddressList) {
        TODO("Not yet implemented")
    }

    override fun subscribeMessage(dest: AdnlIdShort, handler: Adnl.MessageHandler): Adnl.MessageHandler {
        TODO("Not yet implemented")
    }

    override fun subscribeQuery(dest: AdnlIdShort, handler: Adnl.QueryHandler): Adnl.QueryHandler {
        TODO("Not yet implemented")
    }
}
