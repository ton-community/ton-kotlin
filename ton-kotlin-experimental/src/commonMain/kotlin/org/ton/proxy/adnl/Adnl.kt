package org.ton.proxy.adnl

import io.ktor.util.collections.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.encodeHex
import org.ton.logger.Logger
import org.ton.proxy.adnl.channel.AdnlChannel
import org.ton.proxy.adnl.engine.AdnlNetworkEngine
import org.ton.proxy.adnl.resolver.AdnlAddressResolver
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Adnl(
    val networkEngine: AdnlNetworkEngine,
    val addressResolver: AdnlAddressResolver
) : CoroutineScope, AdnlSender {
    override val coroutineContext: CoroutineContext = Dispatchers.Default + CoroutineName(toString())
    private val logger = Logger.println(toString(), Logger.Level.DEBUG)
    private val channels: MutableMap<AdnlIdShort, AdnlChannel> = ConcurrentMap()
    private val remotePeer: MutableMap<AdnlIdShort, AdnlPeerSession> = ConcurrentMap()
    private val localPeer: MutableMap<AdnlIdShort, AdnlPeerSession> = ConcurrentMap()
    private val receiverJob = launch {
        while (isActive) {
            receiveDatagram()
        }
    }
    val startTime = Clock.System.now()

    fun adnlAddressList(): AdnlAddressList {
        val now = Clock.System.now()
        return AdnlAddressList(
            addrs = listOf(),
            version = now.epochSeconds.toInt(),
            reinit_date = startTime.epochSeconds.toInt(),
            expire_at = (now + ADDRESS_LIST_TIMEOUT).epochSeconds.toInt()
        )
    }

    fun registerChannel(
        channelId: AdnlIdShort,
        channel: AdnlChannel
    ): AdnlChannel? = channels.put(channelId, channel)

    fun unregisterChannel(
        channelId: AdnlIdShort,
    ): AdnlChannel? = channels.remove(channelId)

    suspend fun sendDatagram(adnlIdShort: AdnlIdShort, payload: ByteArray) {
        val address = throwUnknownAddress(
            addressResolver.resolve(adnlIdShort)?.second
                ?.filterIsInstance<AdnlAddressUdp>()
                ?.firstOrNull(),
            adnlIdShort
        )
        logger.debug { "send datagram: ${payload.encodeHex()}" }
        networkEngine.sendDatagram(address, ByteReadPacket(payload))
    }

    override suspend fun message(destination: AdnlIdShort, payload: ByteArray) {
        val peer = getPeer(destination)
        return peer.message(payload)
    }

    override suspend fun query(
        destination: AdnlIdShort,
        payload: ByteArray,
        timeout: Duration,
        maxAnswerSize: Int
    ): ByteArray {
        val peer = getPeer(destination)
        return peer.query(payload, timeout)
    }

    private fun <T> throwUnknownAddress(value: T?, adnlIdShort: AdnlIdShort): T = requireNotNull(value) {
        "Unknown address for $adnlIdShort"
    }

    suspend fun getPeer(destination: AdnlIdShort): AdnlPeerSession {
        val (publicKey, _) = throwUnknownAddress(addressResolver.resolve(destination), destination) // save in cache
        require(publicKey is PublicKeyEd25519) { "Unsupported public key type: ${publicKey::class}" }
        val peer = remotePeer.getOrPut(destination) {
            AdnlPeerSession(this, publicKey).also { peer ->
                logger.debug { "Created session: $destination" }
                localPeer[peer.localKey.toAdnlIdShort()] = peer
            }
        }
        return peer
    }

    private suspend fun receiveDatagram() {
        val (address, datagram) = networkEngine.receiveDatagram()
        if (datagram.remaining < 64) {
            logger.warn { "Dropping datagram from: $address, invalid size: ${datagram.remaining}" }
            return
        }
        val id = AdnlIdShort.decode(datagram)
        val peer = localPeer[id]
        if (peer != null) {
            peer.receiveDatagram(datagram.readBytes(), null)
            return
        }
        val channel = channels[id]
        if (channel != null) {
            channel.receiveDatagram(datagram.readBytes())
            return
        }
        logger.warn { "Dropping datagram from: $address, unknown local id: $id" }
    }

    companion object {
        const val MTU = 1024
        val ADDRESS_LIST_TIMEOUT = 1000.seconds
    }
}
