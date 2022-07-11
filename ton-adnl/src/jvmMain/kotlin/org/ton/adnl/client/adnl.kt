package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import org.ton.adnl.aes.AdnlAesCipher
import org.ton.adnl.aes.AesByteReadChannel
import org.ton.adnl.aes.AesByteWriteChannel
import org.ton.adnl.client.AdnlTcpClient
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
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
        val inputCipher = AdnlAesCipher(nonce.copyOfRange(0, 32), nonce.copyOfRange(64, 80))
        val outputCipher = AdnlAesCipher(nonce.copyOfRange(32, 64), nonce.copyOfRange(80, 96))

        val handshake = AdnlHandshake(
            payload = nonce,
            local = PrivateKeyEd25519(SecureRandom.nextBytes(32)),
            other = publicKey
        )
        connection.output.writePacket {
            AdnlHandshake.encode(this, handshake)
        }
        connection.output.flush()

        input = AesByteReadChannel(connection.input, inputCipher)
        output = AesByteWriteChannel(connection.output, outputCipher)

        check(receiveRaw().isEmpty()) { "Invalid handshake response" }
    }
}
