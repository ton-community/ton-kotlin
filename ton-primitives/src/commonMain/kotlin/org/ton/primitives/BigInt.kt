package org.ton.primitives

expect class BigInt : Number

expect fun Number.toBigInt(): BigInt

expect operator fun BigInt.plus(number: Number): BigInt
expect operator fun BigInt.minus(number: Number): BigInt
expect operator fun BigInt.times(number: Number): BigInt
expect operator fun BigInt.div(number: Number): BigInt

fun BigInt(number: Number): BigInt = number.toBigInt()