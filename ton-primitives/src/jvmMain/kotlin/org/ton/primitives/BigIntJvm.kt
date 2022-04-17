package org.ton.primitives

import java.math.BigInteger

typealias BigInt = BigInteger

actual fun Number.toBigInt(): BigInt = when (this) {
    is Long -> BigInteger.valueOf(this)
    is BigInt -> this
    else -> BigInteger.valueOf(this.toLong())
}

actual operator fun BigInt.plus(number: Number): BigInt = add(number.toBigInt())

actual operator fun BigInt.minus(number: Number): BigInt = subtract(number.toBigInt())

actual operator fun BigInt.times(number: Number): BigInt = multiply(number.toBigInt())

actual operator fun BigInt.div(number: Number): BigInt = divide(number.toBigInt())
