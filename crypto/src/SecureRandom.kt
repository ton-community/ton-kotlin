package org.ton.crypto

import kotlin.random.Random

public object SecureRandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        return nextInt().takeUpperBits(bitCount)
    }
}

internal expect fun secureRandom(bytes: ByteArray, fromIndex: Int, toIndex: Int)

internal inline fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)
