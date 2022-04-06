package ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import ton.crypto.hex
import ton.crypto.sha256
import kotlin.random.Random

suspend fun main() {
    test()
}

private suspend fun test() {
//    val private = hex("d08fb02f0dc3e167d6ff114721c4456079310c3141f46f11bdd148fc8c7ef364")
//    println("private: ${hex(private)}")
//    val public3 = Crypto.convertPublicKey(Crypto.generateKeyPair(private).publicKey)
//    println("public3 ${hex(public3)}")

    val clientPrivateKey = AdnlPrivateKey(hex("d08fb02f0dc3e167d6ff114721c4456079310c3141f46f11bdd148fc8c7ef364"))
    val clientPublicKey = clientPrivateKey.public()


//    println("ECDH secret: ${hex(sharedKey.bytes)}")

}

private fun connectAndSend() {
    //    AdnlClient(
//        serverPublicKey = AdnlPublicKey(hex("2615edec7d5d6538314132321a++++++++***2615e1ff5550046e0f1165ff59150632d2301f")),
//        host = "65.21.74.140",
//        port = 46427,
//    ).connect()
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
        val clientPrivateKey = AdnlPrivateKey(hex("d08fb02f0dc3e167d6ff114721c4456079310c3141f46f11bdd148fc8c7ef364"))
        val clientPublicKey = clientPrivateKey.public()
        val sharedKey = clientPrivateKey.sharedKey(serverPublicKey)

        println("local private: ${hex(clientPrivateKey.bytes)}")
        println("local public: ${hex(clientPublicKey.bytes)}")
        println("server public: ${hex(serverPublicKey.bytes)}")
        println("ECDH secret: ${hex(sharedKey.bytes)}")

        connection = aSocket(SelectorManager())
            .tcp()
            .connect(host, port)
            .connection()


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




