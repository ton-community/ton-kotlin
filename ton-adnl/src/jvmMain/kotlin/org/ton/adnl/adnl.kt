package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import kotlin.coroutines.CoroutineContext

class AdnlTcpClientImpl(
    host: String,
    port: Int,
    publicKey: AdnlPublicKey,
    dispatcher: CoroutineContext,
    logger: Logger = PrintLnLogger("TON ADNL")
) : AdnlTcpClient(host, port, publicKey, dispatcher, logger) {
    constructor(
        ipv4: Int,
        port: Int,
        publicKey: AdnlPublicKey,
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

    private suspend fun performHandshake(
        clientPrivateKey: AdnlPrivateKey = AdnlPrivateKey.random(),
        aesParams: AdnlAesParams = AdnlAesParams.random(),
    ) {
        val clientPublicKey = clientPrivateKey.public()
        val sharedKey = clientPrivateKey.sharedKey(publicKey)
        val handshake =
            AdnlHandshake(
                publicKey.address(),
                clientPublicKey,
                aesParams,
                sharedKey
            ).build().readBytes()
        connection.output.writeFully(handshake)
        connection.output.flush()

        input = AesByteReadChannel(connection.input, AdnlAes(aesParams.rxKey, aesParams.rxNonce))
        output = AesByteWriteChannel(connection.output, AdnlAes(aesParams.txKey, aesParams.txNonce))

        check(receiveRaw().isEmpty()) { "Invalid handshake response" }
    }
}
