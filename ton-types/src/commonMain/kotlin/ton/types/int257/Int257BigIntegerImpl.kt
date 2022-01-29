package ton.types.int257

import org.gciatto.kt.math.BigInteger

fun Int257.toBigInteger(): BigInteger = if (this is BigIntegerImpl) {
    bigInteger
} else BigInteger.of(this.toString())

class BigIntegerImpl(
    var bigInteger: BigInteger,
) : Int257 {
    override val sign: Int get() = bigInteger.signum
    override val isNan: Boolean = false
    override val isZero: Boolean = bigInteger == BigInteger.ZERO

    constructor(int257: Int257) : this(BigInteger.of(int257.toString()))

    override fun toInt(): Int = bigInteger.toIntExact()
    override fun toLong(): Long = bigInteger.toLongExact()

    override fun toString(): String = bigInteger.toString()
    override fun toString(radix: Int): String = when (radix) {
        16 -> "0x${bigInteger.toString(16)}"
        2 -> "0b${bigInteger.toString(2)}"
        else -> bigInteger.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Int257) return false
        return compareTo(other) == 0
    }

    override fun plus(other: Int257) = apply {
        bigInteger += other.toBigInteger()
    }

    override fun minus(other: Int257): Int257 = apply {
        bigInteger -= other.toBigInteger()
    }

    override fun unaryMinus(): Int257 = apply {
        bigInteger = -bigInteger
    }

    override fun compareTo(other: Int257): Int = bigInteger.compareTo(other.toBigInteger())

    override fun times(other: Int257): Int257 = apply {
        bigInteger *= other.toBigInteger()
    }

    override fun div(other: Int257): Int257 = apply {
        bigInteger /= other.toBigInteger()
    }

    override infix fun mod(other: Int257): Int257 = apply {
        bigInteger = bigInteger.remainder(other.toBigInteger())
    }

    override infix fun divMod(other: Int257): Pair<Int257, Int257> = let {
        val (div, mod) = bigInteger.divideAndRemainder(other.toBigInteger())
        BigIntegerImpl(div) to BigIntegerImpl(mod)
    }

    override fun shl(other: Int): Int257 = apply {
        bigInteger = bigInteger.shl(other)
    }

    override fun shr(other: Int): Int257 = apply {
        bigInteger = bigInteger.shr(other)
    }

    override fun and(other: Int257): Int257 = apply {
        bigInteger = bigInteger.and(other.toBigInteger())
    }

    override fun or(other: Int257): Int257 = apply {
        bigInteger = bigInteger.or(other.toBigInteger())
    }

    override fun xor(other: Int257): Int257 = apply {
        bigInteger = bigInteger.xor(other.toBigInteger())
    }

    override fun not(): Int257 = apply {
        bigInteger = bigInteger.not()
    }
}