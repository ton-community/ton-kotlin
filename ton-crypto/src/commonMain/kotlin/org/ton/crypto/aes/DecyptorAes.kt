package org.ton.crypto.aes

import org.ton.crypto.Decryptor
import org.ton.crypto.sha256

class DecryptorAes(
    val secret: ByteArray
) : Decryptor {
    override fun decrypt(data: ByteArray): ByteArray {
        check(data.size >= 32) { "data is too short: ${data.size}" }

        val digest = data.copyOfRange(0, 32)
        val key = ByteArray(32)
        secret.copyInto(key, destinationOffset = 0, startIndex = 0, endIndex = 16)
        digest.copyInto(key, destinationOffset = 16, startIndex = 16, endIndex = 32)

        val ctr = ByteArray(16)
        digest.copyInto(ctr, destinationOffset = 0, startIndex = 0, endIndex = 4)
        secret.copyInto(ctr, destinationOffset = 4, startIndex = 20, endIndex = 32)

        val cipher = AesCtr(key, ctr)
        val decryptedData = cipher.encrypt(data)

        val actualDigest = sha256(decryptedData)
        check(digest.contentEquals(actualDigest)) { "sha256 mismatch after decryption" }

        return decryptedData
    }

    override fun sign(message: ByteArray): ByteArray =
        throw IllegalStateException("Can't sign by AES decryptor")
}