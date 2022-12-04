package org.ton.crypto.aes

actual class AesCtr actual constructor(
    key: ByteArray,
    iv: ByteArray
) {
    private val aes = AesCtr256Impl(key, iv)

    actual fun update(input: ByteArray, output: ByteArray): ByteArray =
        aes.encrypt(input, output)

    actual fun doFinal(output: ByteArray): ByteArray = byteArrayOf()

    actual fun doFinal(): ByteArray = byteArrayOf()
}
