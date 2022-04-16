package org.ton.crypto

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun encrypt(byteArray: ByteArray): ByteArray
}