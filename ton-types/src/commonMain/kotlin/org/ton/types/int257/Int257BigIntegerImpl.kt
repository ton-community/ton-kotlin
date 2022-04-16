@file:Suppress("NOTHING_TO_INLINE")

package org.ton.types.int257

import org.ton.common.biginteger.BigInteger

inline fun Int257.toBigInteger(): BigInteger = if (this is BigIntegerImpl) {
    bigInteger
} else BigInteger.of(this.toString())

inline fun BigInteger.toInt257(): Int257 = BigIntegerImpl(this)

@JvmInline
value class BigIntegerImpl(
    val bigInteger: BigInteger,
) : Int257 {
    override val sign: Int get() = bigInteger.signum
    override val isNan: Boolean get() = false
    override val isZero: Boolean get() = bigInteger == BigInteger.ZERO

    override fun toInt(): Int = bigInteger.toIntExact()
    override fun toLong(): Long = bigInteger.toLongExact()

    override fun toString(): String = bigInteger.toString()
    override fun toString(radix: Int): String = when (radix) {
        16 -> "0x${bigInteger.toString(16)}"
        2 -> "0b${bigInteger.toString(2)}"
        else -> bigInteger.toString()
    }

    override fun plus(other: Int257) = (bigInteger + other.toBigInteger()).toInt257()

    override fun minus(other: Int257): Int257 = (bigInteger - other.toBigInteger()).toInt257()

    override fun unaryMinus(): Int257 = (-bigInteger).toInt257()

    override fun compareTo(other: Int257): Int = bigInteger.compareTo(other.toBigInteger())

    override fun times(other: Int257): Int257 = (bigInteger * other.toBigInteger()).toInt257()

    override fun div(other: Int257): Int257 = (bigInteger / other.toBigInteger()).toInt257()

    override infix fun mod(other: Int257): Int257 = bigInteger.remainder(other.toBigInteger()).toInt257()

    override infix fun divMod(other: Int257): Pair<Int257, Int257> = let {
        val (div, mod) = bigInteger.divideAndRemainder(other.toBigInteger())
        BigIntegerImpl(div) to BigIntegerImpl(mod)
    }

    override fun shl(other: Int): Int257 = bigInteger.shl(other).toInt257()

    override fun shr(other: Int): Int257 = bigInteger.shr(other).toInt257()

    override fun and(other: Int257): Int257 = bigInteger.and(other.toBigInteger()).toInt257()

    override fun or(other: Int257): Int257 = bigInteger.or(other.toBigInteger()).toInt257()

    override fun xor(other: Int257): Int257 = bigInteger.xor(other.toBigInteger()).toInt257()

    override fun not(): Int257 = bigInteger.not().toInt257()
}