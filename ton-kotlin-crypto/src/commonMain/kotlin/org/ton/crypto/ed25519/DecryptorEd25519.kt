package org.ton.crypto.ed25519

import org.ton.crypto.Decryptor
import org.ton.crypto.X25519
import org.ton.crypto.aes.DecryptorAes

class DecryptorEd25519(
    private val privateKey: ByteArray
) : Decryptor {
    override fun decrypt(data: ByteArray): ByteArray {
        require(data.size >= Ed25519.KEY_SIZE) { "data is too short: ${data.size}" }
        val publicKey = data.copyOfRange(0, 32)
        val secret = X25519.sharedKey(privateKey, Ed25519.convertToX25519(publicKey))
        val aes = DecryptorAes(secret)
        return aes.decrypt(data.copyOfRange(32, data.size))
    }

    override fun sign(message: ByteArray): ByteArray =
        Ed25519.sign(privateKey, message)
}