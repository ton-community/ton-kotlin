package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PrivateKey
import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import org.ton.crypto.Encryptor
import org.ton.crypto.SecureRandom
import org.ton.crypto.aes.EncryptorAes

class EncryptorEd25519(
    val publicKey: Ed25519PublicKey,
    val privateKey: Ed25519PrivateKey? = null
) : Encryptor {
    constructor(data: ByteArray) : this(Ed25519PublicKey(data))

    override fun encrypt(data: ByteArray): ByteArray {
        val privateKey = privateKey ?: Ed25519.generateKey(SecureRandom)
        val sharedKey = privateKey.sharedKey(publicKey)
        val aes = EncryptorAes(sharedKey)
        val encrypted = aes.encrypt(data)
        return privateKey.publicKey().toByteArray() + encrypted
    }

    override fun verify(message: ByteArray, signature: ByteArray?): Boolean {
        if (signature == null || signature.isEmpty()) return false
        return publicKey.verify(message, signature)
    }
}
