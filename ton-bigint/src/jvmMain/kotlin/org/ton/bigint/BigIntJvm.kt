package org.ton.bigint

import java.math.BigInteger

actual typealias BigInt = BigInteger

actual fun Number.toBigInt(): BigInt = when (this) {
    is Long -> BigInteger.valueOf(this)
    is BigInt -> this
    else -> BigInteger.valueOf(this.toLong())
}

actual val BigInt.bitLength get() = bitLength()

actual operator fun BigInt.plus(number: Number): BigInt = add(number.toBigInt())

actual operator fun BigInt.minus(number: Number): BigInt = subtract(number.toBigInt())

actual operator fun BigInt.times(number: Number): BigInt = multiply(number.toBigInt())

actual operator fun BigInt.div(number: Number): BigInt = divide(number.toBigInt())

actual operator fun BigInt.unaryMinus(): BigInt = negate()

actual infix fun BigInt.shr(shr: Int): BigInt = shiftRight(shr)

actual infix fun BigInt.shl(shl: Int): BigInt = shiftLeft(shl)

actual infix fun BigInt.and(and: BigInt): BigInt = and(and)
