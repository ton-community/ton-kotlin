package org.ton.crypto.ed25519

import org.ton.crypto.Encryptor
import org.ton.crypto.X25519
import org.ton.crypto.aes.EncryptorAes

class EncryptorEd25519(
    private val publicKey: ByteArray
) : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray {
        val privateKey = Ed25519.privateKey()
        val secret = X25519.sharedKey(privateKey, Ed25519.convertToX25519(publicKey))
        val aes = EncryptorAes(secret)
        val encryptedData = aes.encrypt(data)
        return publicKey + encryptedData
    }

    override fun verify(message: ByteArray, signature: ByteArray): Boolean =
        Ed25519.verify(publicKey, message, signature)
}