package org.ton.crypto

import kotlin.random.Random

actual object X25519 {
    actual fun privateKey(random: Random): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun publicKey(privateKey: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun sharedKey(privateKey: ByteArray, publicKey: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun convertToEd25519(publicKey: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

}