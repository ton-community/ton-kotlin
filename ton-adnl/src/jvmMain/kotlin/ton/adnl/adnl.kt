package ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import ton.crypto.Crypto
import ton.crypto.hex
import ton.crypto.sha256
import kotlin.random.Random

suspend fun main() {
    AdnlClient(
        serverPublicKey = AdnlPublicKey(hex("2615edec7d5d6538314132321a2615e1ff5550046e0f1165ff59150632d2301f")),
        host = "65.21.74.140",
        port = 46427,
    ).connect()
}

class AdnlClient(
    val serverPublicKey: AdnlPublicKey,
    val host: String,
    val port: Int,
) {
    lateinit var connection: Connection
    lateinit var txAes: AdnlAes
    lateinit var rxAes: AdnlAes

    suspend fun connect() {
        val clientPrivateKey = AdnlPrivateKey(hex("70580ff7063f80a0b2bd968de5f3465b94de6180c96d6b74502e64f39309ff5e"))
        val clientPublicKey = clientPrivateKey.public()
        val sharedKey = clientPrivateKey.sharedKey(serverPublicKey)


        println("secret: ${hex(clientPrivateKey.bytes)}")
        println("public: ${hex(clientPublicKey.bytes)}")
        println("ed25519 public: ${hex(Crypto.convertEd25519(clientPublicKey.bytes))}")

        connection = aSocket(SelectorManager())
            .tcp()
            .connect(host, port)
            .connection()

        println("connected!")

        val aesParams = AdnlAesParams()
        val handshake =
            AdnlHandshake(serverPublicKey.address(), clientPublicKey, aesParams, sharedKey).build().readBytes()
        println("handshake: ${hex(handshake)}")

        connection.output.writeFully(handshake)
        connection.output.flush()
        println("handshake!")

        txAes = AdnlAes(aesParams.txKey, aesParams.txNonce)
        rxAes = AdnlAes(aesParams.rxKey, aesParams.rxNonce)

//        println("bytes: ${readRemaining.size} ${readRemaining.decodeToString()}")
        val serverTimeQuery =
            hex("7af98bb435263e6c95d6fecb497dfd0aa5f031e7d412986b5ce720496db512052e8f2d100cdf068c7904345aad16000000000000")

        send(serverTimeQuery)
        println("send query...")
        val receive = receive().readBytes()
        println("receive: ${hex(receive)}")
    }

    suspend fun send(packet: ByteArray, nonce: ByteArray = Random.nextBytes(32), flush: Boolean = true) {
        val length = packet.size + 64
        val hash = sha256(nonce, packet)

        val encryptedPacket = txAes.encrypt {
            writeInt(length)
            writeFully(nonce)
            writeFully(packet)
            writeFully(hash)
        }
        connection.output.writeFully(encryptedPacket)
        if (flush) {
            connection.output.flush()
        }
    }

    suspend fun receive() = buildPacket {
        var length = connection.input.readInt()
        length = rxAes.decrypt {
            writeInt(length)
        }.toInt()
        println("length = ${length}")

        var nonce = ByteArray(32)
        connection.input.readFully(nonce)
        nonce = rxAes.decrypt(nonce)
        println("nonce = ${hex(nonce)}")

        var packetBytes = ByteArray(length - 64)
        connection.input.readFully(packetBytes)
        packetBytes = rxAes.decrypt(packetBytes)
        println("packetBytes = ${hex(packetBytes)}")

        var hash = ByteArray(32)
        connection.input.readFully(hash)
        hash = rxAes.decrypt(hash)
        println("hash = ${hex(hash)}")

        val actualHash = sha256(nonce, packetBytes)
        println("actualHash = ${hex(actualHash)}")

        writeFully(packetBytes)
    }
}




