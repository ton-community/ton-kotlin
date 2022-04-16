package org.ton.crypto

import kotlin.random.Random

actual object Ed25519 {
    actual fun privateKey(random: Random): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun publicKey(privateKey: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun sign(privateKey: ByteArray, message: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun verify(
        signature: ByteArray,
        publicKey: ByteArray,
        message: ByteArray
    ): Boolean {
        TODO("Not yet implemented")
    }

    actual fun convertToX25519(publicKey: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}