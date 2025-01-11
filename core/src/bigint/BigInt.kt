package org.ton.bigint

import kotlinx.serialization.Serializable
import org.ton.bigint.serialization.BigIntSerializer

@Serializable(with = BigIntSerializer::class)
public expect class BigInt : Number, Comparable<BigInt> {
    public constructor(value: Long)
    public constructor(value: String)
    public constructor(value: String, radix: Int)
    public constructor(value: ByteArray, sign: Int)

    public val sign: Int
    public val bitLength: Int

    override fun toDouble(): Double
    override fun toFloat(): Float
    override fun toLong(): Long
    override fun toInt(): Int
    override fun toShort(): Short
    override fun toByte(): Byte

    public operator fun plus(other: BigInt): BigInt
    public operator fun plus(other: Long): BigInt

    public operator fun minus(other: BigInt): BigInt
    public operator fun minus(other: Long): BigInt

    public operator fun times(other: BigInt): BigInt
    public operator fun times(other: Long): BigInt

    public operator fun div(other: BigInt): BigInt
    public operator fun div(other: Long): BigInt

    public operator fun rem(other: BigInt): BigInt
    public operator fun rem(other: Long): BigInt

    public operator fun unaryMinus(): BigInt

    public operator fun inc(): BigInt

    public operator fun dec(): BigInt

    public infix fun shl(bitCount: Int): BigInt

    public infix fun shr(bitCount: Int): BigInt

    public fun bitAt(index: Int): Boolean

    public fun pow(exponent: Int): BigInt

    override fun compareTo(other: BigInt): Int

    override fun toString(): String

    public fun toString(radix: Int): String

    override fun equals(other: Any?): Boolean

    public companion object {
        public val ZERO: BigInt
        public val ONE: BigInt
        public val TWO: BigInt
        public val TEN: BigInt
    }
}

public inline fun BigInt.toUByte(): UByte = this.toByte().toUByte()
public inline fun BigInt.toUShort(): UShort = this.toShort().toUShort()
public inline fun BigInt.toUInt(): UInt = this.toInt().toUInt()
public inline fun BigInt.toULong(): ULong = this.toString().toULong()

public inline fun Byte.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun UByte.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun Short.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun UShort.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun Int.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun UInt.toBigInt(): BigInt = BigInt(this.toLong())
public inline fun Long.toBigInt(): BigInt = BigInt(this)
public inline fun ULong.toBigInt(): BigInt = BigInt(this.toString())
