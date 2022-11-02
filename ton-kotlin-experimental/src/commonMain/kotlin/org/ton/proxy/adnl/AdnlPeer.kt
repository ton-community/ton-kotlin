package org.ton.proxy.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.collections.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import org.ton.adnl.ipv4
import org.ton.adnl.packet.AdnlHandshakePacket
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKey
import org.ton.bitstring.BitString
import org.ton.crypto.encodeHex
import org.ton.tl.TLFunction
import org.ton.tl.TlObject
import kotlin.coroutines.resume
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

open class AdnlPeer(
    val address: AdnlAddressUdp,
    val key: PublicKey
) : AdnlPacketReceiver {
    private val startTime = Clock.System.now()
    internal val localKey = PrivateKeyEd25519()
    private val queries = ConcurrentMap<BitString, CompletableDeferred<AdnlMessageAnswer>>()
    private val receiverState = PeerState.receiver(startTime)
    private val senderState = PeerState.sender()
    private val socketAddress = InetSocketAddress(ipv4(address.ip), address.port)
    private val socket = aSocket(SelectorManager()).udp().configure {
        reusePort = true
    }.bind()

    fun start() = GlobalScope.launch {
        while (isActive) {
            val packet = receivePacket()
            receivePacket(packet)
        }
    }

    override fun receiveAnswer(message: AdnlMessageAnswer) {
        val key = BitString(message.query_id)
        val deferred = queries[key]
        deferred?.complete(message)
    }

    private suspend fun receiveDatagram(): Datagram = socket.receive()

    private suspend fun receivePacket(): AdnlPacketContents = coroutineScope {
        var packet: AdnlPacketContents? = null
        while (isActive && packet == null) {
            val datagram = receiveDatagram()
            packet = receiveDatagram(datagram)
        }
        return@coroutineScope suspendCancellableCoroutine {
            if (packet != null) {
                it.resume(packet)
            } else {
                it.cancel()
            }
        }
    }

    private fun receiveDatagram(datagram: Datagram): AdnlPacketContents? {
        val receiverId = datagram.packet.readBytes(32)
        if (receiverId.contentEquals(localKey.toAdnlIdShort().id)) {
            val encryptedData = datagram.packet.readBytes()
            val decryptedData = localKey.decrypt(encryptedData)
            return AdnlPacketContents.decodeBoxed(decryptedData)
        }
        return null
    }

    suspend fun <Q : TLFunction<Q, A>, A : TlObject<A>> query(query: Q): A {
        val queryData = query.toByteArray()
        val answerData = rawQuery(queryData)
        return query.resultTlCodec().decodeBoxed(answerData)
    }

    suspend fun rawQuery(query: ByteArray): ByteArray {
        val queryIdRaw = Random.nextBytes(32)
        val queryId = BitString(queryIdRaw)
        val queryMessage = AdnlMessageQuery(queryIdRaw, query)
        val deferred = CompletableDeferred<AdnlMessageAnswer>()
        queries[queryId] = deferred
        return try {
            coroutineScope {
                while (isActive) {
                    try {
                        sendQuery(queryMessage)
                        return@coroutineScope withTimeout(500.milliseconds) {
                            deferred.await().answer
                        }
                    } catch (e: TimeoutCancellationException) {
                        println("retry query: $queryId")
                    }
                }
                throw CancellationException()
            }
        } finally {
            queries.remove(queryId)
        }
    }

    suspend fun sendQuery(query: AdnlMessageQuery) {
        sendMessages(null, query)
    }

    suspend fun sendMessages(
        channel: Channel?,
        vararg messages: AdnlMessage
    ) = sendMessages(
        channel = channel,
        messages = messages.toList()
    )

    suspend fun sendMessages(
        channel: Channel?,
        messages: List<AdnlMessage>,
    ) {
        val now = Clock.System.now()
        val addressList = AdnlAddressList(
            addrs = listOf(),
            version = now.epochSeconds.toInt(),
            reinit_date = startTime.epochSeconds.toInt(),
            expire_at = (now + ADDRESS_LIST_TIMEOUT).epochSeconds.toInt()
        )
        val packet = AdnlPacketContents(
            from = if (channel == null) localKey.publicKey() else null,
            messages = if (messages.size > 1) messages else null,
            message = if (messages.size == 1) messages.first() else null,
            address = addressList,
            seqno = senderState.ordinaryHistory.seqno++,
            confirm_seqno = receiverState.ordinaryHistory.seqno,
            reinit_date = if (channel == null) startTime.epochSeconds.toInt() else null,
            dst_reinit_date = if (channel == null) senderState.reinitDate.epochSeconds.toInt() else null,
        )
        sendPacket(packet, channel)
    }

    suspend fun sendPacket(packet: AdnlPacketContents, channel: Channel?) {
        val array = packet.toByteArray()
        val decodeBoxed = AdnlPacketContents.decodeBoxed(array)
        check(decodeBoxed == packet)

        val data = if (channel == null) {
            val handshake = AdnlHandshakePacket(
                packet.signed(localKey),
                key
            )
            handshake.build()
        } else {
            ByteReadPacket(key.encrypt(packet.toByteArray()))
        }
        sendDatagram(data)
    }

    suspend fun sendDatagram(data: ByteReadPacket) {
        val datagram = Datagram(data, socketAddress)
        socket.send(datagram)
    }

    companion object {
        val ADDRESS_LIST_TIMEOUT: Duration = 1000.seconds
    }
}
