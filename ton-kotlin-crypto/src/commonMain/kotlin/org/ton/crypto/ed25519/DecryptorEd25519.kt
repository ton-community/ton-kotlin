package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PrivateKey
import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import org.ton.crypto.Decryptor
import org.ton.crypto.aes.DecryptorAes

class DecryptorEd25519(
    val privateKey: Ed25519PrivateKey,
    val publicKey: Ed25519PublicKey? = null
) : Decryptor {
    constructor(data: ByteArray) : this(Ed25519.keyFromSeed(data))

    override fun decrypt(data: ByteArray): ByteArray {
        return if (publicKey == null) {
            val publicKey = Ed25519PublicKey(data)
            val sharedKey = privateKey.sharedKey(publicKey)
            val aes = DecryptorAes(sharedKey)
            aes.decrypt(data.copyOfRange(32, data.size))
        } else {
            val sharedKey = privateKey.sharedKey(publicKey)
            val aes = DecryptorAes(sharedKey)
            aes.decrypt(data)
        }
    }

    override fun sign(message: ByteArray): ByteArray =
        privateKey.sign(message)
}
