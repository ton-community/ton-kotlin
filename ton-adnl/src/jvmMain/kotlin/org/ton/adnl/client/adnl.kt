package org.ton.adnl.client

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import org.ton.adnl.aes.AesByteReadChannel
import org.ton.adnl.aes.AesByteWriteChannel
import org.ton.adnl.ipv4
import org.ton.api.pub.PublicKey
import org.ton.crypto.aes.AesCtr
import org.ton.crypto.encodeHex
import org.ton.crypto.hex
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

    /*
clientPublicKey: 693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d1
sharedKey: e475de82cf819447e30f4f5470b8dccb8fd8edb56b2b44251acd7856882ccf76
receiver: b719c6e9b714d48cad6f068341411593fead76897110dc091f2b1ed47b0b01de
public key: 693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d1
params: 00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
digit: b393978842a0fa3d3e1470196f098f473f9678e72463cb65ec4ab5581856c2e4
key: e475de82cf819447e30f4f5470b8dccb3f9678e72463cb65ec4ab5581856c2e4
ctr: b39397886b2b44251acd7856882ccf76
encrypted: 77f5c6cb396e04fc45d1f087fc3787db3e704fd50b1e4560619e61f20b2fb26ebf842fca9c5bc9a34c060be1624adb63f4d106117a6f90665dc5ef05f1d35c17ae1906002947697477a396fc9fb4cbeac04df842e4dbfebce28c7ddae7fd1ec3c7f6b5eb7020e3fae6ed5c14fc96bd008b6ed390755619d15f61d1561197e89eeb32111e126effcdb84d28948adb1c869396573b81648c0d2dbc38446c6dfbe7
handshake: b719c6e9b714d48cad6f068341411593fead76897110dc091f2b1ed47b0b01de693e47972caf527c7883ad1b39822f026f47db2ab0e1919955b8993aa04411d1b393978842a0fa3d3e1470196f098f473f9678e72463cb65ec4ab5581856c2e477f5c6cb396e04fc45d1f087fc3787db3e704fd50b1e4560619e61f20b2fb26ebf842fca9c5bc9a34c060be1624adb63f4d106117a6f90665dc5ef05f1d35c17ae1906002947697477a396fc9fb4cbeac04df842e4dbfebce28c7ddae7fd1ec3c7f6b5eb7020e3fae6ed5c14fc96bd008b6ed390755619d15f61d1561197e89eeb32111e126effcdb84d28948adb1c869396573b81648c0d2dbc38446c6dfbe7
     */
    private suspend fun performHandshake() {
//        val nonce = if (publicKey is PublicKeyUnencrypted) {
//            ByteArray(160)
//        } else {
//            SecureRandom.nextBytes(160)
//        }
        val nonce = ByteArray(160)
        logger.debug { "nonce: ${hex(nonce)}" }
        val inputCipher = AesCtr(nonce.copyOfRange(0, 32), nonce.copyOfRange(64, 80))
        val outputCipher = AesCtr(nonce.copyOfRange(32, 64), nonce.copyOfRange(80, 96))

        val handshake = buildPacket {
            writeFully(publicKey.toAdnlIdShort().id)
            println("receiver: ${publicKey.toAdnlIdShort().id.encodeHex()}")
            val data = publicKey.encrypt(nonce)
            writeFully(data)
        }.readBytes()
        println("handshake: ${hex(handshake)}")
        connection.output.writeFully(handshake)
        connection.output.flush()

        input = AesByteReadChannel(connection.input, inputCipher)
        output = AesByteWriteChannel(connection.output, outputCipher)

        check(receiveRaw().isEmpty()) { "Invalid handshake response" }
    }
}
