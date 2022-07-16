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
import org.ton.adnl.packet.AdnlPacket
import org.ton.api.adnl.AdnlPacketContents
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

    override suspend fun sendPacket(packet: AdnlPacket) {
        TODO("Not yet implemented")
    }


    override suspend fun receivePacket(): AdnlPacketContents =
        TODO()

    data class Config(
        val host: String = "0.0.0.0",
        val port: Int = 2222,
        val threadsCount: Int = 4
    )
}

@Suppress("OPT_IN_USAGE")
private class AdnlSocket(
    rawInput: DatagramReadChannel,
    rawOutput: DatagramWriteChannel,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    val input = produce<AdnlPacket>(CoroutineName("adnl-aes-input")) {
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

    val output: SendChannel<AdnlPacket> = actor(CoroutineName("adnl-aes-output")) {
        for (record in channel) {
//            if (record.contents.priority_address != null) {
//                priorityOutput.send(record)
//            } else {
//                ordinaryOutput.send(record)
//            }
            ordinaryOutput.send(record)
        }
    }

    private val ordinaryOutput = Channel<AdnlPacket>()
    private val priorityOutput = Channel<AdnlPacket>()

    private val outputJob = launch {
        while (true) {
            try {
                val packet = select {
                    priorityOutput.onReceive { it }
                    ordinaryOutput.onReceive { it }
                }
                TODO()
//                val datagram = Datagram(
//                    packet.build()
//                )
//                packet.datagrams().forEach { datagram ->
//                    println("SEND: ${datagram.packet.copy().readBytes().encodeHex()}")
//                    rawOutput.send(datagram)
//                }
            } catch (cause: Throwable) {
                priorityOutput.close(cause)
                ordinaryOutput.close(cause)
            }
        }
    }
}