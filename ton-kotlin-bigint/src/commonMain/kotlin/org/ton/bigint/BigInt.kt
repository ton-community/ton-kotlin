package org.ton.bigint

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Suppress("ConvertSecondaryConstructorToPrimary")
public expect class BigInt : Number, Comparable<BigInt> {

    public constructor(string: String)
    public constructor(string: String, radix: Int)
    public constructor(byteArray: ByteArray)

    public fun toByteArray(): ByteArray
    public fun toString(radix: Int): String

    public fun not(): BigInt
}

public expect fun Int.toBigInt(): BigInt
public expect fun Long.toBigInt(): BigInt
public expect fun Number.toBigInt(): BigInt

public expect operator fun BigInt.plus(number: Number): BigInt
public expect operator fun BigInt.minus(number: Number): BigInt
public expect operator fun BigInt.times(number: Number): BigInt
public expect operator fun BigInt.div(number: Number): BigInt
public expect operator fun BigInt.unaryMinus(): BigInt
public expect operator fun BigInt.rem(mod: BigInt): BigInt
public expect infix fun BigInt.shr(shr: Int): BigInt
public expect infix fun BigInt.shl(shl: Int): BigInt
public expect infix fun BigInt.and(and: BigInt): BigInt
public expect infix fun BigInt.or(mod: BigInt): BigInt
public expect infix fun BigInt.xor(mod: BigInt): BigInt
public expect infix fun BigInt.divRem(value: BigInt): Array<BigInt>
public expect infix fun BigInt.pow(pow: Int): BigInt
public expect operator fun BigInt.compareTo(other: Int): Int
public expect operator fun BigInt.compareTo(other: Long): Int

public fun BigInt(number: Number): BigInt = number.toBigInt()

public object BigIntSerializer : KSerializer<BigInt> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigInt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BigInt {
        return BigInt(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: BigInt) {
        encoder.encodeString(value.toString())
    }
}

public expect val BigInt.bitLength: Int
public expect val BigInt.sign: Int
public expect val BigInt.isZero: Boolean

public fun BigInt.toUByte(): UByte = toByte().toUByte()
public fun BigInt.toUShort(): UShort = toShort().toUShort()
public fun BigInt.toUInt(): UInt = toInt().toUInt()
public fun BigInt.toULong(): ULong = toLong().toULong()
