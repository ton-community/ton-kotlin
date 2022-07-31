package org.ton.crypto.aes

import io.ktor.utils.io.core.*

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun encrypt(byteArray: ByteArray): ByteArray

    fun encrypt(byteReadPacket: ByteReadPacket): ByteReadPacket
}
