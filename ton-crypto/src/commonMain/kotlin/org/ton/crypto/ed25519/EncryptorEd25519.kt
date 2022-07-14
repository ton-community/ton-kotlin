package org.ton.crypto.ed25519

import org.ton.crypto.Encryptor
import org.ton.crypto.X25519
import org.ton.crypto.aes.EncryptorAes
import org.ton.crypto.encodeHex
import org.ton.crypto.hex

class EncryptorEd25519(
    private val publicKey: ByteArray
) : Encryptor {
    override fun encrypt(data: ByteArray): ByteArray {
//        val privateKey = Ed25519.privateKey()
        println("other public: ${hex(publicKey)}")
        val privateKey = ByteArray(32)
        val localPublic = Ed25519.publicKey(privateKey)
        println("local public: ${hex(localPublic)}")
        val secret = X25519.sharedKey(privateKey, Ed25519.convertToX25519(publicKey))
        println("secret: ${secret.encodeHex()}")
        val aes = EncryptorAes(secret)
        val encryptedData = aes.encrypt(data)
        return (localPublic + encryptedData).also {
            println("result ed25519: ${it.encodeHex()}")
        }
    }

    override fun verify(message: ByteArray, signature: ByteArray): Boolean =
        Ed25519.verify(publicKey, message, signature)
}