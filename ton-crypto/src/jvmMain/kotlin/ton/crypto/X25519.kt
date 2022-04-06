package ton.crypto

import curve25519.MontgomeryPoint
import org.bouncycastle.math.ec.rfc7748.X25519
import kotlin.random.Random

actual object X25519 {
    actual fun privateKey(random: Random): ByteArray = random.nextBytes(32)

    actual fun publicKey(privateKey: ByteArray): ByteArray {
        val publicKey = ByteArray(32)
        X25519.generatePublicKey(privateKey, 0, publicKey, 0)
        return publicKey
    }

    actual fun sharedKey(privateKey: ByteArray, publicKey: ByteArray): ByteArray {
        val sharedKey = ByteArray(32)
        X25519.calculateAgreement(privateKey, 0, publicKey, 0, sharedKey, 0)
        return sharedKey
    }

    actual fun convertToEd25519(publicKey: ByteArray): ByteArray {
        val edwardsPoint = requireNotNull(MontgomeryPoint(publicKey).toEdwardsPoint(0)) { "Invalid X25519 public key" }
        return edwardsPoint.compress().toByteArray()
    }
}