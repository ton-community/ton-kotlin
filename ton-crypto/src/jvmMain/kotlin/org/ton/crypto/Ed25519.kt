package org.ton.crypto

import curve25519.CompressedEdwardsY
import kotlin.random.Random

actual object Ed25519 {
    actual fun privateKey(random: Random): ByteArray = random.nextBytes(32)

    actual fun publicKey(privateKey: ByteArray): ByteArray {
        val publicKey = ByteArray(32)
        org.bouncycastle.math.ec.rfc8032.Ed25519.generatePublicKey(privateKey, 0, publicKey, 0)
        return publicKey
    }

    actual fun sign(privateKey: ByteArray, message: ByteArray): ByteArray {
        val signature = ByteArray(64)
        org.bouncycastle.math.ec.rfc8032.Ed25519.sign(privateKey, 0, message, 0, message.size, signature, 0)
        return signature
    }

    actual fun verify(signature: ByteArray, publicKey: ByteArray, message: ByteArray): Boolean {
        return org.bouncycastle.math.ec.rfc8032.Ed25519.verify(signature, 0, publicKey, 0, message, 0, message.size)
    }

    actual fun convertToX25519(publicKey: ByteArray): ByteArray {
        val edwardsPoint = requireNotNull(CompressedEdwardsY(publicKey).decompress()) { "Invalid Ed25519 public key" }
        return edwardsPoint.toMontgomeryPoint().toByteArray()
    }
}