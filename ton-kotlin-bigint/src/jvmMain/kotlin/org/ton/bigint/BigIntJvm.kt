package org.ton.bigint

import java.math.BigInteger

actual typealias BigInt = BigInteger

actual fun Number.toBigInt(): BigInt = when (this) {
    is Long -> BigInteger.valueOf(this)
    is Int -> BigInteger.valueOf(this.toLong())
    is Byte -> BigInteger.valueOf(this.toLong())
    is Short -> BigInteger.valueOf(this.toLong())
    is BigInt -> this
    else -> BigInteger.valueOf(this.toLong())
}

actual val BigInt.bitLength get() = bitLength()
actual val BigInt.sign get() = signum()
actual val BigInt.isZero get() = this == BigInteger.ZERO

actual operator fun BigInt.plus(number: Number): BigInt = add(number.toBigInt())

actual operator fun BigInt.minus(number: Number): BigInt = subtract(number.toBigInt())

actual operator fun BigInt.times(number: Number): BigInt = multiply(number.toBigInt())

actual operator fun BigInt.div(number: Number): BigInt = divide(number.toBigInt())

actual operator fun BigInt.unaryMinus(): BigInt = negate()

actual infix fun BigInt.shr(shr: Int): BigInt = shiftRight(shr)

actual infix fun BigInt.shl(shl: Int): BigInt = shiftLeft(shl)

actual infix fun BigInt.and(and: BigInt): BigInt = and(and)

actual infix fun BigInt.mod(mod: BigInt): BigInt = mod(mod)

actual infix fun BigInt.or(mod: BigInt): BigInt = or(mod)

actual infix fun BigInt.xor(mod: BigInt): BigInt = xor(mod)

actual infix fun BigInt.pow(pow: Int): BigInt = pow(pow)

// TODO: check all cases
actual infix fun BigInt.divRem(value: BigInt): Array<BigInt> = divideAndRemainder(value)
