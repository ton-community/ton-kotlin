package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.coroutines.CoroutineContext

class AdnlTcpClientImpl(
        host: String,
        port: Int,
        publicKey: AdnlPublicKey,
        dispatcher: CoroutineContext
) : AdnlTcpClient(host, port, publicKey, dispatcher) {
    private lateinit var connection: Connection

    override suspend fun connect() = apply {
        connection = aSocket(SelectorManager(dispatcher))
                .tcp()
                .connect(host, port)
                .connection()
        performHandshake()
        job = launchReceiveJob()
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

        check(receiveRaw().isEmpty) { "Invalid handshake response" }
    }
}
