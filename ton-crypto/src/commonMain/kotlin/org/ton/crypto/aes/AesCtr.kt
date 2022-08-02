package org.ton.crypto.aes

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun encrypt(byteArray: ByteArray): ByteArray
}
