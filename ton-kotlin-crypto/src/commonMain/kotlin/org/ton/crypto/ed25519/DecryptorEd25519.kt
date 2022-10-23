package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import org.ton.crypto.Decryptor
import org.ton.crypto.aes.DecryptorAes

class DecryptorEd25519(
    privateKey: ByteArray
) : Decryptor {
    private val privateKey = Ed25519.keyFromSeed(privateKey)

    override fun decrypt(data: ByteArray): ByteArray {
        require(data.size >= Ed25519.SEED_SIZE_BYTES) { "data is too short: ${data.size}" }
        val publicKey = Ed25519PublicKey(data)
        val sharedKey = privateKey.sharedKey(publicKey)
        val aes = DecryptorAes(sharedKey)
        return aes.decrypt(data.copyOfRange(32, data.size))
    }

    override fun sign(message: ByteArray): ByteArray =
        privateKey.sign(message)
}
