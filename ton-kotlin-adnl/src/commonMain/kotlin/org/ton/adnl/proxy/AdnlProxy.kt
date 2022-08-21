package org.ton.adnl.proxy

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.ton.api.adnl.*
import org.ton.api.adnl.proxy.AdnlProxy
import org.ton.logger.Logger
import org.ton.tl.constructors.readInt256Tl
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


class AdnlProxyImpl(
    val inPort: Int,
    val outPort: Int,
    val proxy: AdnlProxy,
    val address: InetSocketAddress,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val logger = Logger.println("ADNL PROXY")

    val outSeqno = atomic(0L)
    var inUdpServer: UdpServer? = null
    var outUdpServer: UdpServer? = null

    init {
        if (inPort == outPort) {
            inUdpServer = UdpServer(inPort) {
                onUdpMessage(it, 0)
            }
        } else {
            inUdpServer = UdpServer(inPort) {
                onUdpMessage(it, 1)
            }
            outUdpServer = UdpServer(outPort) {
                onUdpMessage(it, 2)
            }
        }
    }

    private fun startTime() = Clock.System.now().epochSeconds.toInt()

    private suspend fun onUdpMessage(datagram: Datagram, mode: Int) {
        when (mode) {
            0 -> receiveCommon(datagram)
            1 -> receiveFromClient(datagram)
            else -> receiveToClient(datagram)
        }
    }

    private suspend fun receiveCommon(datagram: Datagram) {
        if (datagram.packet.remaining > 32) {
            logger.info { "dropping too short packet: size=${datagram.packet.remaining}" }
            return
        }
        if (proxy.id.contentEquals(datagram.packet.copy().readInt256Tl())) {
            receiveFromClient(datagram)
        } else {
            receiveToClient(datagram)
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun receiveFromClient(datagram: Datagram) {
        val addr = datagram.address as InetSocketAddress
        val input = datagram.packet
        val packetHeader = AdnlProxyPacketHeader.tlConstructor().decode(input)
        if (packetHeader.flags and AdnlProxyPacketHeader.OUTBOUND_MASK != 0) {
            logger.info { "proxy: dropping message from client: outbound flag is set" }
            return
        }
        val date = packetHeader.date?.let {
            Instant.fromEpochSeconds(it.toLong())
        }
        if (date != null) {
            val now = Clock.System.now()
            if (date + 60.seconds < now || date - 60.seconds > now) {
                logger.info { "proxy: dropping message from client: date mismatch" }
                return
            }
        }
        if (packetHeader.isControl) {
            val controlPacket = AdnlProxyControlPacket.decodeBoxed(input)
            when (controlPacket) {
                is AdnlProxyControlPacketPing -> {
                    val pong = AdnlProxyControlPacketPong(controlPacket.id)
//                    val header = AdnlProxyPacketHeader(
//                        proxy_id = proxy.id,
//                        ip = ipv4(addr.hostname),
//                        port = addr.port,
//                        adnl_start_time = startTime(),
//                        seqno = outSeqno.value,
//                        signature = ByteArray(32)
//                    )
                }

                is AdnlProxyControlPacketPong -> TODO()
                is AdnlProxyControlPacketRegister -> TODO()
            }
        }
    }

    private suspend fun receiveToClient(datagram: Datagram) {

    }

    suspend fun decrypt(data: ByteReadPacket) {

    }


    enum class Mode {
        COMMON,
        FROM_CLIENT,
        TO_CLIENT
    }
}
