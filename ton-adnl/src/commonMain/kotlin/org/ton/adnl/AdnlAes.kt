package org.ton.adnl

import io.ktor.utils.io.core.*
import org.ton.crypto.AesCtr

class AdnlAes(
        key: ByteArray,
        iv: ByteArray,
) {
    private val backend = AesCtr(key, iv)

    suspend fun encrypt(packet: suspend BytePacketBuilder.() -> Unit): ByteReadPacket {
        val builder = BytePacketBuilder()
        packet(builder)
        val encrypted = encrypt(builder.build().readBytes())
        return ByteReadPacket(encrypted)
    }

    fun encrypt(byteArray: ByteArray): ByteArray {
        if (byteArray.isEmpty()) return byteArray
        return backend.encrypt(byteArray)
    }
}