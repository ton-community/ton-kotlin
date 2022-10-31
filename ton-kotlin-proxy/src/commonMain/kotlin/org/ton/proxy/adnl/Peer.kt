package org.ton.proxy.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.ton.adnl.ipv4
import org.ton.adnl.packet.AdnlHandshakePacket
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.encodeHex
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Peer(
    val address: AdnlAddressUdp,
    val key: PublicKeyEd25519
) {
    private val startTime = Clock.System.now()
    private val localKey = PrivateKeyEd25519()
    private val receiverState = PeerState.receiver(startTime)
    private val senderState = PeerState.sender()
    private val socketAddress = InetSocketAddress(ipv4(address.ip), address.port)
    private val socket = aSocket(SelectorManager()).udp().bind()

    private val job = GlobalScope.launch {
        while (isActive) {
            val datagram = socket.receive()
            println(datagram.packet.readBytes().encodeHex())
        }
    }

    suspend fun sendPacket(
        channel: Channel?,
        vararg messages: AdnlMessage
    ) = sendPacket(
        channel = channel,
        messages = messages.toList()
    )

    suspend fun sendPacket(
        channel: Channel?,
        messages: List<AdnlMessage>,
    ) {
        val randBytes = Random.nextBytes(10)
        val now = Clock.System.now()
        val addressList = AdnlAddressList(
            addrs = listOf(address),
            version = now.epochSeconds.toInt(),
            reinit_date = startTime.epochSeconds.toInt(),
            expire_at = (now + ADDRESS_LIST_TIMEOUT).epochSeconds.toInt()
        )
        val packet = AdnlPacketContents(
            from = if (channel == null) localKey.publicKey() else null,
            messages = messages,
            address = addressList,
            seqno = senderState.ordinaryHistory.seqno++,
            confirm_seqno = receiverState.ordinaryHistory.seqno,
            reinit_date = if (channel == null) startTime.epochSeconds.toInt() else null,
            dst_reinit_date = if (channel == null) null else senderState.reinitDate.epochSeconds.toInt(),
        )
        val rawPacket = if (channel == null) {
            val handshake = AdnlHandshakePacket(
                packet.signed(localKey),
                key
            )
            handshake.build()
        } else {
            ByteReadPacket(key.encrypt(packet.toByteArray()))
        }
        val datagram = Datagram(rawPacket, socketAddress)
        socket.send(datagram)
    }

    companion object {
        val ADDRESS_LIST_TIMEOUT: Duration = 1000.seconds
    }
}
