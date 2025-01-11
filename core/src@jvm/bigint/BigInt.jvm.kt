package org.ton.bigint

import kotlinx.serialization.Serializable
import org.ton.bigint.serialization.BigIntSerializer

@Suppress("NOTHING_TO_INLINE")
@Serializable(with = BigIntSerializer::class)
public actual class BigInt @PublishedApi
internal constructor(
    public val javaBigInt: java.math.BigInteger
) : Number(), Comparable<BigInt> {
    public actual constructor(value: Long) : this(java.math.BigInteger.valueOf(value))

    public actual constructor(value: String) : this(java.math.BigInteger(value))

    public actual constructor(value: String, radix: Int) : this(java.math.BigInteger(value, radix))

    public actual constructor(value: ByteArray, sign: Int) : this(java.math.BigInteger(sign, value))

    public actual val sign: Int get() = javaBigInt.signum()
    public actual val bitLength: Int get() = javaBigInt.bitLength()

    actual override fun toDouble(): Double = javaBigInt.toDouble()

    actual override fun toFloat(): Float = javaBigInt.toFloat()

    actual override fun toLong(): Long = javaBigInt.toLong()

    actual override fun toInt(): Int = javaBigInt.toInt()

    actual override fun toShort(): Short = javaBigInt.toShort()

    actual override fun toByte(): Byte = javaBigInt.toByte()

    public actual inline operator fun plus(other: BigInt): BigInt = BigInt(javaBigInt.add(other.javaBigInt))
    public actual inline operator fun plus(other: Long): BigInt = BigInt(javaBigInt.add(java.math.BigInteger.valueOf(other)))

    public actual inline operator fun minus(other: BigInt): BigInt =
        BigInt(javaBigInt.subtract(other.javaBigInt))
    public actual inline operator fun minus(other: Long): BigInt =
        BigInt(javaBigInt.subtract(java.math.BigInteger.valueOf(other)))

    public actual inline operator fun times(other: BigInt): BigInt =
        BigInt(javaBigInt.multiply(other.javaBigInt))
    public actual inline operator fun times(other: Long): BigInt =
        BigInt(javaBigInt.multiply(java.math.BigInteger.valueOf(other)))

    public actual inline operator fun div(other: BigInt): BigInt = BigInt(javaBigInt.divide(other.javaBigInt))
    public actual inline operator fun div(other: Long): BigInt =
        BigInt(javaBigInt.divide(java.math.BigInteger.valueOf(other)))

    public actual inline operator fun rem(other: BigInt): BigInt =
        BigInt(javaBigInt.remainder(other.javaBigInt))

    public actual inline operator fun rem(other: Long): BigInt =
        BigInt(javaBigInt.remainder(java.math.BigInteger.valueOf(other)))

    public actual inline operator fun unaryMinus(): BigInt =
        BigInt(javaBigInt.negate())

    public actual inline operator fun inc(): BigInt =
        BigInt(javaBigInt.add(java.math.BigInteger.ONE))

    public actual inline operator fun dec(): BigInt =
        BigInt(javaBigInt.subtract(java.math.BigInteger.ONE))

    public actual infix fun shl(bitCount: Int): BigInt = BigInt(javaBigInt.shiftLeft(bitCount))

    public actual infix fun shr(bitCount: Int): BigInt = BigInt(javaBigInt.shiftRight(bitCount))

    public actual fun bitAt(index: Int): Boolean = javaBigInt.testBit(index)

    public actual fun pow(exponent: Int): BigInt = BigInt(javaBigInt.pow(exponent))

    actual override fun compareTo(other: BigInt): Int = javaBigInt.compareTo(other.javaBigInt)

    actual override fun toString(): String = javaBigInt.toString()

    public actual fun toString(radix: Int): String = javaBigInt.toString(radix)

    actual override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BigInt) return false
        return javaBigInt == other.javaBigInt
    }

    public actual companion object {
        public actual val ZERO: BigInt = BigInt(java.math.BigInteger.ZERO)
        public actual val ONE: BigInt = BigInt(java.math.BigInteger.ONE)
        public actual val TWO: BigInt = BigInt(java.math.BigInteger.TWO)
        public actual val TEN: BigInt = BigInt(java.math.BigInteger.TEN)
    }
}

@Suppress("NOTHING_TO_INLINE")
public inline fun BigInt.toJavaBigInt(): java.math.BigInteger = javaBigInt

@Suppress("NOTHING_TO_INLINE")
public inline fun java.math.BigInteger.toKotlinBigInt(): BigInt = BigInt(this)