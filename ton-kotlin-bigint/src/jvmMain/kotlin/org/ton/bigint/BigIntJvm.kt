package org.ton.bigint

import java.math.BigInteger

public actual typealias BigInt = BigInteger

public actual fun Int.toBigInt(): BigInt = BigInteger.valueOf(this.toLong())
public actual fun Long.toBigInt(): BigInt = BigInteger.valueOf(this)

public actual fun Number.toBigInt(): BigInt = when (this) {
    is Long -> BigInteger.valueOf(this)
    is Int -> BigInteger.valueOf(this.toLong())
    is Byte -> BigInteger.valueOf(this.toLong())
    is Short -> BigInteger.valueOf(this.toLong())
    is BigInt -> this
    else -> BigInteger.valueOf(this.toLong())
}

public actual val BigInt.bitLength get() = bitLength()
public actual val BigInt.sign get() = signum()
public actual val BigInt.isZero get() = this == BigInteger.ZERO

public actual operator fun BigInt.plus(number: Number): BigInt = add(number.toBigInt())

public actual operator fun BigInt.minus(number: Number): BigInt = subtract(number.toBigInt())

public actual operator fun BigInt.times(number: Number): BigInt = multiply(number.toBigInt())

public actual operator fun BigInt.div(number: Number): BigInt = divide(number.toBigInt())

public actual operator fun BigInt.unaryMinus(): BigInt = negate()

public actual infix fun BigInt.shr(shr: Int): BigInt = shiftRight(shr)

public actual infix fun BigInt.shl(shl: Int): BigInt = shiftLeft(shl)

public actual infix fun BigInt.and(and: BigInt): BigInt = and(and)

public actual operator fun BigInt.rem(mod: BigInt): BigInt = mod(mod)

public actual infix fun BigInt.or(mod: BigInt): BigInt = or(mod)

public actual infix fun BigInt.xor(mod: BigInt): BigInt = xor(mod)

public actual infix fun BigInt.pow(pow: Int): BigInt = pow(pow)

public actual operator fun BigInt.compareTo(other: Int): Int = compareTo(other.toBigInt())
public actual operator fun BigInt.compareTo(other: Long): Int = compareTo(other.toBigInt())

// TODO: check all cases
public actual infix fun BigInt.divRem(value: BigInt): Array<BigInt> = divideAndRemainder(value)
