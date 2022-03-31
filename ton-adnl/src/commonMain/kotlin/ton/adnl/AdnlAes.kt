package ton.adnl

import io.ktor.utils.io.core.*

interface AdnlAes {
    val key: ByteArray
    val nonce: ByteArray

    suspend fun encrypt(packet: BytePacketBuilder.() -> Unit): ByteArray =
        encrypt(buildPacket(block = packet).readBytes())

    suspend fun encrypt(byteArray: ByteArray): ByteArray
    suspend fun decrypt(packet: BytePacketBuilder.() -> Unit): ByteArray =
        decrypt(buildPacket(block = packet).readBytes())

    suspend fun decrypt(byteArray: ByteArray): ByteArray
}

expect fun AdnlAes(key: ByteArray, nonce: ByteArray): AdnlAes