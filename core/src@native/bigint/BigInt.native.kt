package org.ton.kotlin.bigint

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlinx.serialization.Serializable
import org.ton.kotlin.bigint.serialization.BigIntSerializer

@Serializable(with = BigIntSerializer::class)
public actual class BigInt internal constructor(
    private val value: BigInteger
) : Number(), Comparable<BigInt> {
    public actual constructor(value: Long) : this(BigInteger.fromLong(value))

    public actual constructor(value: String) : this(BigInteger.parseString(value))

    public actual constructor(value: String, radix: Int) : this(BigInteger.parseString(value, radix))

    public actual constructor(value: ByteArray, sign: Int) : this(
        BigInteger.fromByteArray(
            value,
            if (sign >= 0) Sign.POSITIVE else Sign.NEGATIVE
        )
    )

    public actual val sign: Int = value.signum()
    public actual val bitLength: Int = value.bitLength()

    actual override fun toDouble(): Double = value.doubleValue()
    actual override fun toFloat(): Float = value.floatValue()
    actual override fun toLong(): Long = value.longValue()
    actual override fun toInt(): Int = value.intValue()
    actual override fun toShort(): Short = value.shortValue()
    actual override fun toByte(): Byte = value.byteValue()

    public actual fun toByteArray(): ByteArray = value.toByteArray()

    public actual operator fun plus(other: BigInt): BigInt = BigInt(value.add(other.value))
    public actual operator fun plus(other: Long): BigInt = BigInt(value.add(BigInteger.fromLong(other)))

    public actual operator fun minus(other: BigInt): BigInt = BigInt(value.subtract(other.value))
    public actual operator fun minus(other: Long): BigInt = BigInt(value.subtract(BigInteger.fromLong(other)))

    public actual operator fun times(other: BigInt): BigInt = BigInt(value.multiply(other.value))
    public actual operator fun times(other: Long): BigInt = BigInt(value.multiply(BigInteger.fromLong(other)))

    public actual operator fun div(other: BigInt): BigInt = BigInt(value.divide(other.value))
    public actual operator fun div(other: Long): BigInt = BigInt(value.divide(BigInteger.fromLong(other)))

    public actual operator fun rem(other: BigInt): BigInt = BigInt(value.remainder(other.value))
    public actual operator fun rem(other: Long): BigInt = BigInt(value.remainder(BigInteger.fromLong(other)))

    public actual operator fun unaryMinus(): BigInt = BigInt(value.negate())

    public actual operator fun inc(): BigInt = BigInt(value.inc())

    public actual operator fun dec(): BigInt = BigInt(value.dec())

    public actual infix fun shl(bitCount: Int): BigInt = BigInt(value shl bitCount)

    public actual infix fun shr(bitCount: Int): BigInt = BigInt(value shr bitCount)

    public actual fun bitAt(index: Int): Boolean = value.bitAt(index.toLong())

    public actual fun pow(exponent: Int): BigInt = BigInt(value.pow(exponent))

    actual override fun compareTo(other: BigInt): Int = value.compareTo(other.value)

    actual override fun toString(): String = value.toString()

    public actual fun toString(radix: Int): String = value.toString(radix)

    actual override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BigInt) return false
        return value == other.value
    }

    public actual companion object {
        public actual val ZERO: BigInt = BigInt(BigInteger.ZERO)
        public actual val ONE: BigInt = BigInt(BigInteger.ONE)
        public actual val TWO: BigInt = BigInt(BigInteger.TWO)
        public actual val TEN: BigInt = BigInt(BigInteger.TEN)
    }
}