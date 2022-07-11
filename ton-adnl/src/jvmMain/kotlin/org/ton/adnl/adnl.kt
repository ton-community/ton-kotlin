package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import org.ton.api.pk.PrivateKeyAes
import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
import org.ton.crypto.sha256
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

        val handshake = buildHandshakePayload(nonce)
        connection.output.writeFully(handshake)
        connection.output.flush()

        input = AesByteReadChannel(connection.input, inputCipher)
        output = AesByteWriteChannel(connection.output, outputCipher)

        check(receiveRaw().isEmpty()) { "Invalid handshake response" }
    }

    private fun buildHandshakePayload(
        nonce: ByteArray,
    ): ByteArray {
        val checksum = sha256(nonce)
        val buf = nonce.copyOf(96)
        val local = PrivateKeyAes(SecureRandom.nextBytes(32))

        publicKey.toAdnlIdShort().id.copyInto(buf, destinationOffset = 0, startIndex = 0, endIndex = 32)
        local.publicKey().key.copyInto(buf, destinationOffset = 32, startIndex = 0, endIndex = 32)
        checksum.copyInto(buf, destinationOffset = 64, startIndex = 0, endIndex = 32)

        val sharedSecret = local.sharedSecret(publicKey)
        val cipher = AdnlAesCipher.packetCipher(sharedSecret, checksum)
        return buf + cipher.encrypt(nonce)
    }
}
