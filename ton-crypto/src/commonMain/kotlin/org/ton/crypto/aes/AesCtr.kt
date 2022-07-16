package org.ton.crypto.aes

import io.ktor.utils.io.core.*

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun encrypt(byteArray: ByteArray): ByteArray

    suspend fun encrypt(packet: suspend BytePacketBuilder.() -> Unit): ByteReadPacket
}