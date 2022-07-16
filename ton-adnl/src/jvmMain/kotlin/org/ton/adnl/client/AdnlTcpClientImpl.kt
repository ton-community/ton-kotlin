package org.ton.adnl.client

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import org.ton.adnl.aes.AesByteReadChannel
import org.ton.adnl.aes.AesByteWriteChannel
import org.ton.adnl.ipv4
import org.ton.adnl.packet.AdnlHandshakePacket
import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
import org.ton.crypto.aes.AesCtr
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import kotlin.coroutines.CoroutineContext

class AdnlTcpClientImpl(
    host: String,
    port: Int,
    publicKey: PublicKey,
    dispatcher: CoroutineContext,
    logger: Logger = PrintLnLogger("TON ADNL")
) : AdnlTcpClient(host, port, publicKey, dispatcher, logger) {
    constructor(
        ipv4: Int,
        port: Int,
        publicKey: PublicKey,
        dispatcher: CoroutineContext,
        logger: Logger = PrintLnLogger("TON ADNL")
    ) : this(
        ipv4(ipv4),
        port,
        publicKey,
        dispatcher,
        logger
    )

    private lateinit var connection: Connection

    override suspend fun connect() = apply {
        logger.debug { "Connecting... $host:$port" }
        connection = aSocket(SelectorManager(dispatcher))
            .tcp()
            .connect(host, port)
            .connection()
        logger.debug { "Connected! Performing handshake... $publicKey" }
        performHandshake()
        logger.debug { "Success handshake!" }
        launchIoJob()
    }

    override suspend fun disconnect() {
        supervisorJob.cancelAndJoin()
        withContext(Dispatchers.IO) {
            connection.socket.close()
        }
        connection.socket.awaitClosed()
    }

    private suspend fun performHandshake() {
        val nonce = SecureRandom.nextBytes(160)
        val inputCipher = AesCtr(nonce.copyOfRange(0, 32), nonce.copyOfRange(64, 80))
        val outputCipher = AesCtr(nonce.copyOfRange(32, 64), nonce.copyOfRange(80, 96))
        val handshake = AdnlHandshakePacket(nonce, publicKey)

        connection.output.writePacket(handshake.build())
        connection.output.flush()

        input = AesByteReadChannel(connection.input, inputCipher)
        output = AesByteWriteChannel(connection.output, outputCipher)

        check(receiveRaw().isEmpty()) { "Invalid handshake response" }
    }
}
