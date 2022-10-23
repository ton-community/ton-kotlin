package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import io.github.andreypfau.curve25519.x25519.X25519
import org.ton.crypto.Encryptor
import org.ton.crypto.SecureRandom
import org.ton.crypto.aes.EncryptorAes
import org.ton.crypto.ed25519.Ed25519.convertToEd25519

class EncryptorEd25519(
    publicKey: ByteArray
) : Encryptor {
    private val publicKey = Ed25519PublicKey(publicKey)

    override fun encrypt(data: ByteArray): ByteArray {
        val privateKey = Ed25519.privateKey(SecureRandom)
        val decryptionKey = convertToEd25519(X25519.x25519(privateKey))
        val secret = X25519.x25519(privateKey, X25519.toX25519(publicKey))
        val aes = EncryptorAes(secret)
        val encryptedData = aes.encrypt(data)
        return decryptionKey + encryptedData
    }

    override fun verify(message: ByteArray, signature: ByteArray?): Boolean {
        if (signature == null || signature.isEmpty()) return false
        return publicKey.verify(message, signature)
    }
}
