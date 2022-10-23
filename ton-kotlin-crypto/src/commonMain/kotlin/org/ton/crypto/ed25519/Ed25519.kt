package org.ton.crypto.ed25519

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PublicKey
import io.github.andreypfau.curve25519.edwards.CompressedEdwardsY
import io.github.andreypfau.curve25519.montgomery.MontgomeryPoint
import org.ton.crypto.SecureRandom
import kotlin.random.Random

object Ed25519 {
    const val KEY_SIZE_BYTES = 32

    fun privateKey(random: Random = SecureRandom): ByteArray =
        Ed25519.generateKey(random).seed()

    fun publicKey(privateKey: ByteArray): ByteArray =
        Ed25519.keyFromSeed(privateKey).publicKey().toByteArray()

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Ed25519.keyFromSeed(privateKey).sign(message)

    fun verify(publicKey: ByteArray, message: ByteArray, signature: ByteArray) =
        Ed25519PublicKey(publicKey).verify(message, signature)

    fun convertToEd25519(publicKey: ByteArray): ByteArray {
        val montgomeryPoint = MontgomeryPoint(publicKey)
        val edwardsPoint = montgomeryPoint.toEdwards(0)
        val compressedEdwardsY = CompressedEdwardsY.from(edwardsPoint)
        return compressedEdwardsY.data
    }
}
