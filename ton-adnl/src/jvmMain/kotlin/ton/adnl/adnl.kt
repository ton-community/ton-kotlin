package ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import ton.crypto.Crypto
import ton.crypto.hex

suspend fun main() {
    AdnlClient(
        publicKey = hex("2615edec7d5d6538314132321a2615e1ff5550046e0f1165ff59150632d2301f"),
        host = "65.21.74.140",
        port = 46427,
    ).connect()
}

class AdnlClient(
    val publicKey: ByteArray,
    val host: String,
    val port: Int,
) {
    lateinit var socket: Socket

    suspend fun connect() {
        socket = aSocket(SelectorManager())
            .tcp()
            .connect(host, port)

        val localSecret = hex("70580ff7063f80a0b2bd968de5f3465b94de6180c96d6b74502e64f39309ff5e")
        val (privateKey, publicKey) = Crypto.generateKeyPair(localSecret)
        println("private: ${hex(privateKey)}")
        println("public: ${hex(publicKey)}")

        val serverTimeQuery =
            hex("7af98bb435263e6c95d6fecb497dfd0aa5f031e7d412986b5ce720496db512052e8f2d100cdf068c7904345aad16000000000000")
    }
}




