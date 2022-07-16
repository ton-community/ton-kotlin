package org.ton.crypto.ed25519

import org.ton.crypto.Encryptor
import org.ton.crypto.X25519
import org.ton.crypto.aes.EncryptorAes

class EncryptorEd25519(
    private val publicKey: ByteArray
) : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray {
        val privateKey = Ed25519.privateKey()
        val decryptionKey = X25519.convertToEd25519(X25519.publicKey(privateKey))
        val secret = X25519.sharedKey(privateKey, Ed25519.convertToX25519(publicKey))
        val aes = EncryptorAes(secret)
        val encryptedData = aes.encrypt(data)
        return decryptionKey + encryptedData
    }

    override fun verify(message: ByteArray, signature: ByteArray?): Boolean {
        if (signature == null || signature.isEmpty()) return false
        return Ed25519.verify(publicKey, message, signature)
    }
}