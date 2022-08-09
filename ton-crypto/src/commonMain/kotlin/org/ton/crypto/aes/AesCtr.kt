package org.ton.crypto.aes

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun update(input: ByteArray): ByteArray
    fun doFinal(): ByteArray
}
