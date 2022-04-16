package org.ton.types.int257

import org.ton.common.biginteger.BigInteger

interface Int257 {
    val sign: Int
    val isNan: Boolean
    val isZero: Boolean
    override fun equals(other: Any?): Boolean
    override fun toString(): String
    fun toString(radix: Int): String

    operator fun plus(other: Int257): Int257
    operator fun minus(other: Int257): Int257
    operator fun times(other: Int257): Int257
    operator fun div(other: Int257): Int257
    operator fun unaryMinus(): Int257
    operator fun compareTo(other: Int257): Int

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

fun int257(boolean: Boolean) = if (boolean) int257("true") else int257("false")

fun int257(value: String): Int257 = when (value) {
    "0" -> BigIntegerImpl(BigInteger.ZERO)
    "1" -> BigIntegerImpl(BigInteger.ONE)
    "2" -> BigIntegerImpl(BigInteger.TWO)
    "-1" -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    "10" -> BigIntegerImpl(BigInteger.TEN)
    "false" -> BigIntegerImpl(BigInteger.ZERO)
    "true" -> BigIntegerImpl(BigInteger.NEGATIVE_ONE)
    else -> {
        val bigInteger = when {
            value[0] == '0' && value[1] == 'x' -> BigInteger(value.substring(2), 16)
            value[0] == '-' && value[1] == '0' && value[2] == 'x' -> -BigInteger(value.substring(3), 16)
            value[0] == '0' && value[1] == 'b' -> BigInteger(value.substring(2), 2)
            value[0] == '-' && value[1] == '0' && value[2] == 'b' -> -BigInteger(value.substring(3), 2)
            else -> try {
                BigInteger(value)
            } catch (e: NumberFormatException) {
                throw NumberFormatException("Illegal number: `$value`")
            }
        }
        BigIntegerImpl(bigInteger)
    }
}