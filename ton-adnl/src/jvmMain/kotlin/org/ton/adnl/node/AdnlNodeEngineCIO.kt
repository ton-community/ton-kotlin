package org.ton.adnl.node

import io.ktor.client.utils.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.ton.adnl.AdnlHandshake
import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlAddressTunnel
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlAddressUdp6
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.pk.PrivateKeyEd25519
import kotlin.coroutines.CoroutineContext

class AdnlNodeEngineCIO(
    val config: Config = Config()
) : AdnlNodeEngineBase("adnl-cio") {

    @OptIn(InternalAPI::class)
    override val dispatcher by lazy {
        Dispatchers.clientDispatcher(config.threadsCount, "adnl-cio-dispatcher")
    }

    private val selectorManager by lazy {
        SelectorManager(dispatcher)
    }
    private lateinit var socket: AdnlSocket

    override fun start() {
        val rawSocket = aSocket(selectorManager).udp().bind(InetSocketAddress(config.host, config.port))
        socket = AdnlSocket(
            rawSocket,
            rawSocket,
            coroutineContext
        )
    }

    override suspend fun sendPacket(
        peer: AdnlPeer,
        packet: AdnlPacketContents,
        subChannelSide: AdnlSubChannelSide?
    ) {
        val datagram = AdnlDatagram(
            packet,
            peer,
            subChannelSide
        )
        socket.output.send(datagram)
    }

    data class Config(
        val host: String = "0.0.0.0",
        val port: Int = 2222,
        val threadsCount: Int = 4
    )
}


private class AdnlDatagram(
    val packet: AdnlPacketContents,
    val peer: AdnlPeer,
    val subChannelSide: AdnlSubChannelSide? = null
) {
    fun toDatagram(): Datagram {
        val payload = buildPacket {
            val data = packet.toByteArray()
            if (subChannelSide != null) {
                writeFully(subChannelSide.encrypt(data))
            } else {
                val key = PrivateKeyEd25519.random()
                val handshake = AdnlHandshake(data, key, peer.node.id)
                AdnlHandshake.encode(this, handshake)
            }
        }
        val socketAddress = socketAddress()
        return Datagram(payload, socketAddress)
    }

    private fun socketAddress(): SocketAddress =
        when (val address = peer.address) {
            is AdnlAddressUdp -> InetSocketAddress(ipv4(address.ip), address.port)
            is AdnlAddressUdp6 -> TODO()
            is AdnlAddressTunnel -> TODO()
        }
}

@Suppress("OPT_IN_USAGE")
private class AdnlSocket(
    rawInput: DatagramReadChannel,
    rawOutput: DatagramWriteChannel,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    private val input = produce<AdnlDatagram>(CoroutineName("adnl-aes-input")) {
        try {
            loop@ while (true) {

            }
        } catch (cause: ClosedReceiveChannelException) {
            channel.close()
        } catch (cause: Throwable) {
            channel.close()
        } finally {
            outputJob.cancel()
        }
    }

    val output: SendChannel<AdnlDatagram> = actor(CoroutineName("adnl-aes-output")) {
        for (datagram in channel) {
            if (datagram.subChannelSide == null) {
                rawOutput.send(datagram.toDatagram())
            } else {
                if (datagram.subChannelSide.priority) {
                    priorityOutput.send(datagram)
                } else {
                    ordinaryOutput.send(datagram)
                }
            }
        }
    }

    private val ordinaryOutput = Channel<AdnlDatagram>()
    private val priorityOutput = Channel<AdnlDatagram>()

    private val outputJob = launch {
        while (true) {
            try {
                val packet = select {
                    priorityOutput.onReceive { it }
                    ordinaryOutput.onReceive { it }
                }
                val datagram = packet.toDatagram()
                rawOutput.send(datagram)
            } catch (cause: Throwable) {
                priorityOutput.close(cause)
                ordinaryOutput.close(cause)
            }
        }
    }
}