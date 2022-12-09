package org.ton.crypto.random

import kotlin.random.Random

expect object SecureRandom : Random

@Suppress("NOTHING_TO_INLINE")
internal inline fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)
