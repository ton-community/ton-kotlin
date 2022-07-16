package org.ton.crypto

interface Decryptor {
    fun decrypt(data: ByteArray): ByteArray
    fun sign(message: ByteArray): ByteArray
}

object DecryptorNone : Decryptor {
    override fun decrypt(data: ByteArray): ByteArray = data
    override fun sign(message: ByteArray): ByteArray = ByteArray(0)
}

object DecryptorFail : Decryptor {
    override fun decrypt(data: ByteArray): ByteArray =
        throw IllegalStateException("Fail decryptor")

    override fun sign(message: ByteArray): ByteArray =
        throw IllegalStateException("Fail decryptor")
}