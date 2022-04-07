package ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import ton.crypto.hex
import ton.crypto.sha256

class AdnlClient(
    val serverPublicKey: AdnlPublicKey,
    val host: String,
    val port: Int,
) {
    lateinit var connection: Connection
    lateinit var input: ByteReadChannel
    lateinit var output: ByteWriteChannel

    suspend fun connect() = apply {
        connection = aSocket(SelectorManager())
            .tcp()
            .connect(host, port)
            .connection()
        performHandshake()
    }

    suspend fun send(packet: ByteArray, nonce: ByteArray = ByteArray(32), flush: Boolean = true) {
        val length = packet.size + 64
        val hash = sha256(nonce, packet)
        val encryptedPacket = buildPacket {
            writeIntLittleEndian(length)
            writeFully(nonce)
            writeFully(packet)
            writeFully(hash)
        }
        output.writePacket(encryptedPacket)
        if (flush) {
            output.flush()
        }
    }

    suspend fun receive(block: ByteReadPacket.() -> Unit = {}): ByteReadPacket {
        val length = input.readIntLittleEndian()

        check(length >= 64) { "Too small packet: $length" }
        check(length <= Short.MAX_VALUE) { "Too big packet: $length" }

        val nonce = input.readPacket(32).readBytes()
        val payload = input.readPacket(length - 64).readBytes()
        val hash = input.readPacket(32).readBytes()

        val actualHash = sha256(nonce, payload)
        check(hash.contentEquals(actualHash)) { "Invalid hash! expected: ${hex(hash)} actual: ${hex(actualHash)}" }

        return ByteReadPacket(payload).apply(block)
    }

    private suspend fun performHandshake(
        clientPrivateKey: AdnlPrivateKey = AdnlPrivateKey.random(),
        aesParams: AdnlAesParams = AdnlAesParams.random(),
    ) {
        val clientPublicKey = clientPrivateKey.public()
        val sharedKey = clientPrivateKey.sharedKey(serverPublicKey)
        val handshake =
            AdnlHandshake(
                serverPublicKey.address(),
                clientPublicKey,
                aesParams,
                sharedKey
            ).build().readBytes()
        connection.output.writeFully(handshake)
        connection.output.flush()

        input = EncryptedByteReadChannel(connection.input, AdnlAes(aesParams.rxKey, aesParams.rxNonce))
        output = EncryptedByteWriteChannel(connection.output, AdnlAes(aesParams.txKey, aesParams.txNonce))

        check(receive().isEmpty) { "Invalid handshake response" }
    }
}
