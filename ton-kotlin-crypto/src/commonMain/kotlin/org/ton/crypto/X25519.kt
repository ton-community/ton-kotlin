package org.ton.crypto

import kotlin.random.Random

expect object X25519 {
    fun privateKey(random: Random = SecureRandom): ByteArray

    fun publicKey(privateKey: ByteArray): ByteArray

    fun sharedKey(privateKey: ByteArray, publicKey: ByteArray): ByteArray

    fun convertToEd25519(publicKey: ByteArray): ByteArray
}