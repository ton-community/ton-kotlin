package org.ton.bigint

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Suppress("ConvertSecondaryConstructorToPrimary")
expect class BigInt : Number, Comparable<BigInt> {
    constructor(string: String)
    constructor(string: String, radix: Int)
    constructor(byteArray: ByteArray)

    fun toByteArray(): ByteArray
}

expect fun Number.toBigInt(): BigInt

expect operator fun BigInt.plus(number: Number): BigInt
expect operator fun BigInt.minus(number: Number): BigInt
expect operator fun BigInt.times(number: Number): BigInt
expect operator fun BigInt.div(number: Number): BigInt
expect operator fun BigInt.unaryMinus(): BigInt
expect infix fun BigInt.shr(shr: Int): BigInt
expect infix fun BigInt.shl(shl: Int): BigInt
expect infix fun BigInt.and(and: BigInt): BigInt

fun BigInt(number: Number): BigInt = number.toBigInt()

object BigIntSerializer : KSerializer<BigInt> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigInt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BigInt {
        return BigInt(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: BigInt) {
        encoder.encodeString(value.toString())
    }
}

expect val BigInt.bitLength: Int
expect val BigInt.sign: Int
