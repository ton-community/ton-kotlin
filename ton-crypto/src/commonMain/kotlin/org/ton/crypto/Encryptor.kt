package org.ton.crypto

interface Encryptor {
    fun encrypt(data: ByteArray): ByteArray
    fun verify(message: ByteArray, signature: ByteArray): Boolean
}

object EncryptorNone : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray = data
    override fun verify(message: ByteArray, signature: ByteArray): Boolean = true
}

object EncryptorFail : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray =
        throw IllegalStateException("Fail encryptor")

    override fun verify(message: ByteArray, signature: ByteArray): Boolean =
        throw IllegalStateException("Fail encryptor")
}