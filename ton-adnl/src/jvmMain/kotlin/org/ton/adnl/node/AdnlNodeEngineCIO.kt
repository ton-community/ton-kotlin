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
            println("ENCRYPT: ${hex(data)}")
            val payload = buildPacket {
                writeFully(subChannelSide.encrypt(data))
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
                val key = PrivateKeyEd25519(ByteArray(32))
                val handshake = AdnlHandshake(data, key, publicKey)
                AdnlHandshake.encode(this, handshake)
            }
            Datagram(payload, address)
        }
    }
}
// 40f6f8b76359216893243cdb88fb67548ca6962fb20faa7a0662f45b2f4950ee693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d13ade19ccdcc7884400612664379aa659f14472ddd89c589c5636d270bbc0097ace8462a3e94080d5fac039d1df16b00fc55d16aef5ccd2f907e9633c4062f7d9db193e6e1a0ce75fcfa433f9cee47ae55ce7bcdb9d89ab414c963673864a240b28d873e2a8a33189dd99b2dd95144df095e254d5ed6bcd25b7feea93546e8896d180e2ee7f339a56025300bd0127cf9b07bf2db412a5efe4e1a343c7f77dd3d5daad0f48cc5fd2199a3d70acea8de9930b2b7a8ff60c9a2076e43f26e485173cf63e76f5782d44e1b69f9de7be035c196df259566ef9d76c19e3633022c724bfdbf0f543820e78c9b53971b1fd104a5dc8b543a264629d71b406b842bcad71687e64393992484e28a4f06705090469499bae365300beff901d8f8800c11adc63de4fac8a1b4b29a972e869980c43ea07674a292245415c6b
// 40f6f8b76359216893243cdb88fb67548ca6962fb20faa7a0662f45b2f4950ee693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d13ade19ccdcc7884400612664379aa659f14472ddd89c589c5636d270bbc0097ace8462a3e94080d5fac039d1df16b00fc55d16aef5ccd2f907e9633c4062f7d9db193e6e1a0ce75fcfa433f9cee47ae55ce7bcdb9d89ab414c963673864a240b28d873e2a8a33189dd99b2dd95144df095e254d5ed6bcd25b7feea93546e8896d180e2ee7f339a56025300bd0127cf9b07bf2db412a5efe4e1a343c7f77dd3d5daad0f48cc5fd2199a3d70acea8de9930b2b7a8ff60c9a2076e43f26e485173cf63e76f5782d44e1b69f9de7be035c196df259566ef9d76c19e3633022c724bfdbf0f543820e78c9b53971b1fd104a5dc8b543a264629d71b406b842bcad71687e64393992484e28a4f06705090469499bae365300beff901d8f8800c11adc63de4fac8a1b4b29a972e869980c43ea07674a292245415c6b
//                                                                 693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d13ade19ccdcc7884400612664379aa659f14472ddd89c589c5636d270bbc0097ace8462a3e94080d5fac039d1df16b00fc55d16aef5ccd2f907e9633c4062f7d9db193e6e1a0ce75fcfa433f9cee47ae55ce7bcdb9d89ab414c963673864a240b28d873e2a8a33189dd99b2dd95144df095e254d5ed6bcd25b7feea93546e8896d180e2ee7f339a56025300bd0127cf9b07bf2db412a5efe4e1a343c7f77dd3d5daad0f48cc5fd2199a3d70acea8de9930b2b7a8ff60c9a2076e43f26e485173cf63e76f5782d44e1b69f9de7be035c196df259566ef9d76c19e3633022c724bfdbf0f543820e78c9b53971b1fd104a5dc8b543a264629d71b406b842bcad71687e64393992484e28a4f06705090469499bae365300beff901d8f8800c11adc63de4fac8a1b4b29a972e869980c43ea07674a292245415c6b

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