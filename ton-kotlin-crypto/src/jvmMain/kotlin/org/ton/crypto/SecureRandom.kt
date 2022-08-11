package org.ton.crypto

import kotlin.random.Random

actual object SecureRandom : Random() {
    private val javaSecureRandom = java.security.SecureRandom.getInstanceStrong()

    override fun nextBits(bitCount: Int): Int {
        return javaSecureRandom.nextInt() ushr (Int.SIZE_BITS - bitCount)
    }
}
