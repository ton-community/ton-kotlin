package org.ton.crypto

import kotlin.random.Random

public object SecureRandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        return nextInt().takeUpperBits(bitCount)
    }

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        secureRandom(array, fromIndex, toIndex)
        return array
    }

    override fun nextInt(): Int {
        val bytes = ByteArray(4)
        secureRandom(bytes, 0, bytes.size)
        var result = bytes[0].toInt()
        result = result or (bytes[1].toInt() shl 8)
        result = result or (bytes[2].toInt() shl 16)
        result = result or (bytes[3].toInt() shl 24)
        return result
    }
}

internal expect fun secureRandom(array: ByteArray, fromIndex: Int, toIndex: Int)

internal inline fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)
