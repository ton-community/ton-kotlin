package org.ton.bigint

@Suppress("ConvertSecondaryConstructorToPrimary")
public actual class BigInt : Number, Comparable<BigInt> {
    public actual constructor(string: String) {
        TODO("Not yet implemented")
    }

    public actual constructor(string: String, radix: Int) {
        TODO("Not yet implemented")
    }

    public actual constructor(byteArray: ByteArray) {
        TODO("Not yet implemented")
    }

    internal constructor(magnitude: IntArray, signum: Int) {
        TODO()
    }

    public actual fun toByteArray(): ByteArray {
        TODO("Not yet implemented")
    }

    public actual fun toString(radix: Int): String {
        TODO("Not yet implemented")
    }

    public actual fun not(): BigInt {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: BigInt): Int {
        TODO("Not yet implemented")
    }

    override fun toByte(): Byte {
        TODO("Not yet implemented")
    }

    override fun toChar(): Char {
        TODO("Not yet implemented")
    }

    override fun toDouble(): Double {
        TODO("Not yet implemented")
    }

    override fun toFloat(): Float {
        TODO("Not yet implemented")
    }

    override fun toInt(): Int {
        TODO("Not yet implemented")
    }

    override fun toLong(): Long {
        TODO("Not yet implemented")
    }

    override fun toShort(): Short {
        TODO("Not yet implemented")
    }

    companion object {
        const val LONG_MASK = 0xffffffffL
        val ZERO = BigInt(intArrayOf(), 0)
        val ONE = BigInt(intArrayOf(1), 1)

        fun bitLengthForInt(n: Int): Int = 32 - n.countLeadingZeroBits()
    }
}

public actual fun Number.toBigInt(): BigInt {
    TODO("Not yet implemented")
}

public actual fun Int.toBigInt(): BigInt {
    TODO("Not yet implemented")
}

public actual fun Long.toBigInt(): BigInt {
    TODO("Not yet implemented")
}

public actual val BigInt.bitLength: Int get() = TODO()
public actual val BigInt.sign: Int get() = TODO()
public actual val BigInt.isZero: Boolean get() = TODO()

public actual operator fun BigInt.plus(number: Number): BigInt = TODO()
public actual operator fun BigInt.minus(number: Number): BigInt = TODO()
public actual operator fun BigInt.times(number: Number): BigInt = TODO()
public actual operator fun BigInt.div(number: Number): BigInt = TODO()
public actual operator fun BigInt.unaryMinus(): BigInt = TODO()
public actual operator fun BigInt.rem(mod: BigInt): BigInt = TODO()
public actual infix fun BigInt.shr(shr: Int): BigInt = TODO()
public actual infix fun BigInt.shl(shl: Int): BigInt = TODO()
public actual infix fun BigInt.and(and: BigInt): BigInt = TODO()
public actual infix fun BigInt.or(mod: BigInt): BigInt = TODO()
public actual infix fun BigInt.xor(mod: BigInt): BigInt = TODO()
public actual infix fun BigInt.divRem(value: BigInt): Array<BigInt> = TODO()
public actual infix fun BigInt.pow(pow: Int): BigInt = TODO()
public actual operator fun BigInt.compareTo(other: Int): Int = TODO()
public actual operator fun BigInt.compareTo(other: Long): Int = TODO()
