package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import io.github.andreypfau.curve25519.edwards.CompressedEdwardsY
import io.github.andreypfau.curve25519.edwards.EdwardsPoint
import io.github.andreypfau.curve25519.montgomery.MontgomeryPoint
import org.ton.crypto.SecureRandom
import kotlin.random.Random

object Ed25519 {
    const val KEY_SIZE_BYTES = 32

    @JvmStatic
    fun privateKey(random: Random = SecureRandom): ByteArray =
        Ed25519.generateKey(random).seed()

    @JvmStatic
    fun publicKey(privateKey: ByteArray): ByteArray =
        Ed25519.keyFromSeed(privateKey).publicKey().toByteArray()

    @JvmStatic
    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Ed25519.keyFromSeed(privateKey).sign(message)

    @JvmStatic
    fun verify(publicKey: ByteArray, message: ByteArray, signature: ByteArray) =
        Ed25519PublicKey(publicKey).verify(message, signature)

    @JvmStatic
    fun sharedKey(privateKey: ByteArray, publicKey: ByteArray): ByteArray =
        Ed25519.keyFromSeed(privateKey).sharedKey(Ed25519PublicKey(publicKey))
}
