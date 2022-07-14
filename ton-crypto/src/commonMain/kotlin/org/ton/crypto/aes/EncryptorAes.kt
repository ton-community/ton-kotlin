package org.ton.crypto.aes

import org.ton.crypto.Encryptor
import org.ton.crypto.encodeHex
import org.ton.crypto.sha256

class EncryptorAes(
    val secret: ByteArray
) : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray {
        val digest = sha256(data)
        println("params: ${data.encodeHex()}")
        println("digest: ${digest.encodeHex()}")
        val key = ByteArray(32)
        secret.copyInto(key, destinationOffset = 0, startIndex = 0, endIndex = 16)
        digest.copyInto(key, destinationOffset = 16, startIndex = 16, endIndex = 32)

        val ctr = ByteArray(16)
        digest.copyInto(ctr, destinationOffset = 0, startIndex = 0, endIndex = 4)
        secret.copyInto(ctr, destinationOffset = 4, startIndex = 20, endIndex = 32)

        println("key: ${key.encodeHex()}")
        println("ctr: ${ctr.encodeHex()}")

        val cipher = AesCtr(key, ctr)
        val encryptedData = cipher.encrypt(data)

        println("encrypted aes: ${encryptedData.encodeHex()}")

        return (digest + encryptedData).also {
            println("result aes: ${it.encodeHex()}")
        }
    }

    override fun verify(message: ByteArray, signature: ByteArray): Boolean =
        throw IllegalStateException("Can't verify by AES encryptor")
}