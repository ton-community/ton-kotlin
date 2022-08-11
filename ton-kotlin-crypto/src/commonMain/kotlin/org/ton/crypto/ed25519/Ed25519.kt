package org.ton.crypto.ed25519

import org.ton.crypto.SecureRandom
import kotlin.random.Random

expect object Ed25519 {
    fun privateKey(random: Random = SecureRandom): ByteArray
    fun publicKey(privateKey: ByteArray): ByteArray
    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray
    fun verify(publicKey: ByteArray, message: ByteArray, signature: ByteArray): Boolean
    fun convertToX25519(publicKey: ByteArray): ByteArray
}

val Ed25519.KEY_SIZE: Int get() = 32