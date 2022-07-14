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
import org.ton.adnl.ipv4
import org.ton.api.adnl.*
import org.ton.api.pub.PublicKey
import org.ton.crypto.encodeHex
import kotlin.coroutines.CoroutineContext

class AdnlNodeEngineCIO(
    val config: Config = Config()
) : AdnlNodeEngineBase("adnl-cio") {

    @OptIn(InternalAPI::class)
    override val dispatcher by lazy {
        Dispatchers.clientDispatcher(config.threadsCount, "adnl-cio-dispatcher")
    }

    val selectorManager by lazy {
        SelectorManager(dispatcher)
    }
    private lateinit var socket: AdnlSocket

    override fun start() {
        val rawSocket = aSocket(selectorManager).udp().bind(InetSocketAddress(config.host, config.port))
        start(rawSocket)
    }

    fun start(datagramChannel: DatagramReadWriteChannel) {
        socket = AdnlSocket(
            datagramChannel,
            datagramChannel,
            coroutineContext
        )
    }

    override suspend fun sendPacket(
        packet: AdnlPacketContents,
        subChannelSide: AdnlSubChannelSide
    ) {
        val record = AdnlChannelRecord(
            packet,
            subChannelSide
        )
        socket.output.send(record)
    }

    override suspend fun sendPacket(packet: AdnlPacketContents, otherKey: PublicKey) {
        val record = AdnlHandshakeRecord(
            packet,
            otherKey
        )
        socket.output.send(record)
    }

    override suspend fun receivePacket(): AdnlPacketContents =
        socket.input.receive().packet

    data class Config(
        val host: String = "0.0.0.0",
        val port: Int = 2222,
        val threadsCount: Int = 4
    )
}

private interface AdnlRecord {
    val packet: AdnlPacketContents

    fun datagrams(): Sequence<Datagram>

    fun socketAddresses(): Sequence<SocketAddress> {
        val adnlAddressSequence =
            packet.priority_address?.addrs?.asSequence() ?: packet.address?.addrs?.asSequence() ?: emptySequence()
        return adnlAddressSequence.map {
            when (it) {
                is AdnlAddressUdp -> InetSocketAddress(ipv4(it.ip), it.port)
                is AdnlAddressUdp6 -> TODO()
                is AdnlAddressTunnel -> TODO()
            }
        }
    }
}

private class AdnlChannelRecord(
    override val packet: AdnlPacketContents,
    val subChannelSide: AdnlSubChannelSide
) : AdnlRecord {
    override fun datagrams(): Sequence<Datagram> {
        val data = packet.toByteArray()
        return socketAddresses().map { address ->
            val payload = buildPacket {
                writeFully(subChannelSide.key.encrypt(data))
            }
            Datagram(payload, address)
        }
    }
}

private class AdnlHandshakeRecord(
    override val packet: AdnlPacketContents,
    val publicKey: PublicKey
) : AdnlRecord {
    override fun datagrams(): Sequence<Datagram> {
        val data = packet.toByteArray()
        return socketAddresses().map { address ->
            val payload = buildPacket {
                writeFully(AdnlIdShort.encodeBoxed(publicKey.toAdnlIdShort()))
                writeFully(publicKey.encrypt(data))
            }
            Datagram(payload, address)
        }
    }
}

@Suppress("OPT_IN_USAGE")
private class AdnlSocket(
    rawInput: DatagramReadChannel,
    rawOutput: DatagramWriteChannel,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    val input = produce<AdnlRecord>(CoroutineName("adnl-aes-input")) {
        try {
            loop@ while (true) {
                val datagram = rawInput.receive()
                val data = datagram.packet.readBytes()
                println("RECV: ${hex(data)}")
            }
        } catch (cause: ClosedReceiveChannelException) {
            channel.close()
        } catch (cause: Throwable) {
            channel.close()
        } finally {
            outputJob.cancel()
        }
    }

    val output: SendChannel<AdnlRecord> = actor(CoroutineName("adnl-aes-output")) {
        for (record in channel) {
            if (record.packet.priority_address != null) {
                priorityOutput.send(record)
            } else {
                ordinaryOutput.send(record)
            }
        }
    }

    private val ordinaryOutput = Channel<AdnlRecord>()
    private val priorityOutput = Channel<AdnlRecord>()

    private val outputJob = launch {
        while (true) {
            try {
                val record = select {
                    priorityOutput.onReceive { it }
                    ordinaryOutput.onReceive { it }
                }
                record.datagrams().forEach { datagram ->
                    println("SEND: ${datagram.packet.copy().readBytes().encodeHex()}")
                    rawOutput.send(datagram)
                }
            } catch (cause: Throwable) {
                priorityOutput.close(cause)
                ordinaryOutput.close(cause)
            }
        }
    }
}