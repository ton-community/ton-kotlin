package ton.fift

import org.gciatto.kt.math.BigInteger

interface Int257 {
    val sign: Int
    val isNan: Boolean
    override fun toString(): String
    fun toString(radix: Int): String

    operator fun plus(other: Int257): Int257
    operator fun minus(other: Int257): Int257
    operator fun times(other: Int257): Int257
    operator fun div(other: Int257): Int257
    operator fun unaryMinus(): Int257

    fun toInt(): Int
    fun toLong(): Long
    infix fun mod(other: Int257): Int257
    infix fun divMod(other: Int257): Pair<Int257, Int257>
    infix fun shl(other: Int): Int257
    infix fun shr(other: Int): Int257
    infix fun and(other: Int257): Int257
    infix fun or(other: Int257): Int257
    infix fun xor(other: Int257): Int257
    fun not(): Int257
}

fun Int.toInt257() = int257(this)
fun Long.toInt257() = int257(this)

fun nan(): Int257 = NanInt257
fun int257(value: Int): Int257 = when (value) {
    0 -> BigIntegerImpl(BigInteger.ZERO)
    1 -> BigIntegerImpl(BigInteger.ONE)
    2 -> BigIntegerImpl(BigInteger.TWO)
    -1 -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    10 -> BigIntegerImpl(BigInteger.TEN)
    else -> BigIntegerImpl(BigInteger.of(value))
}

fun int257(value: Long): Int257 = when (value) {
    0L -> BigIntegerImpl(BigInteger.ZERO)
    1L -> BigIntegerImpl(BigInteger.ONE)
    2L -> BigIntegerImpl(BigInteger.TWO)
    -1L -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    10L -> BigIntegerImpl(BigInteger.TEN)
    else -> BigIntegerImpl(BigInteger.of(value))
}

fun int257(value: String): Int257 = when (value) {
    "0" -> BigIntegerImpl(BigInteger.ZERO)
    "1" -> BigIntegerImpl(BigInteger.ONE)
    "2" -> BigIntegerImpl(BigInteger.TWO)
    "-1" -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    "10" -> BigIntegerImpl(BigInteger.TEN)
    "false" -> BigIntegerImpl(BigInteger.ZERO)
    "true" -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    "bl" -> BigIntegerImpl(BigInteger.of(32))
    else -> {
        val bigInteger = when {
            value[0] == '0' && value[1] == 'x' -> BigInteger(value.substring(2), 16)
            value[0] == '-' && value[1] == '0' && value[2] == 'x' -> -BigInteger(value.substring(3), 16)
            value[0] == '0' && value[1] == 'b' -> BigInteger(value.substring(2), 2)
            value[0] == '-' && value[1] == '0' && value[2] == 'b' -> -BigInteger(value.substring(3), 2)
            else -> BigInteger(value)
        }
        BigIntegerImpl(bigInteger)
    }
}

object NanInt257 : Int257 {
    override val sign: Int = 0
    override val isNan: Boolean = true
    override fun toString(): String = "NaN"
    override fun toString(radix: Int): String = toString()

    override fun plus(other: Int257) = NanInt257
    override fun minus(other: Int257) = NanInt257
    override fun unaryMinus(): Int257 = NanInt257
    override fun times(other: Int257) = NanInt257
    override fun div(other: Int257) = NanInt257
    override fun mod(other: Int257) = NanInt257
    override fun divMod(other: Int257): Pair<NanInt257, NanInt257> = NanInt257 to NanInt257
    override fun shl(other: Int) = NanInt257
    override fun shr(other: Int) = NanInt257
    override fun and(other: Int257) = NanInt257
    override fun or(other: Int257) = NanInt257
    override fun xor(other: Int257) = NanInt257
    override fun not() = NanInt257

    override fun toInt(): Int = throw ArithmeticException("NaN")
    override fun toLong(): Long = throw ArithmeticException("NaN")
}

fun Int257.toBigInteger(): BigInteger = if (this is BigIntegerImpl) {
    bigInteger
} else BigInteger.of(this.toString())

class BigIntegerImpl(
    var bigInteger: BigInteger,
) : Int257 {
    override val sign: Int get() = bigInteger.signum
    override val isNan: Boolean = false

    constructor(int257: Int257) : this(BigInteger.of(int257.toString()))

    override fun toInt(): Int = bigInteger.toIntExact()
    override fun toLong(): Long = bigInteger.toLongExact()

    override fun toString(): String = bigInteger.toString()
    override fun toString(radix: Int): String = when (radix) {
        16 -> "0x${bigInteger.toString(16)}"
        2 -> "0b${bigInteger.toString(2)}"
        else -> bigInteger.toString()
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