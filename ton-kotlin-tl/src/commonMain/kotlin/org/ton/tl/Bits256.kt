package org.ton.tl

import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import kotlin.jvm.JvmInline

@JvmInline
@Serializable(with = Bits256.Companion::class)
public value class Bits256(
    public val value: BitString = BitString(256)
) : BitString by value {
    public constructor(value: ByteArray) : this(value.toBitString())

    init {
        require(value.size == 256) { "Bits256 must be 256 bits long" }
    }

    public inline fun toByteArray(): ByteArray = value.toByteArray()
    public inline fun toBitString(): BitString = value

    override fun toString(): String = base64()

    public fun hex(): String = value.toString()
    public fun base64(): String = value.toByteArray().encodeBase64()

    override fun xor(other: BitString): Bits256 = Bits256(value xor other)

    public companion object : KSerializer<Bits256> by Bits256Serializer
}

public object Bits256Serializer : KSerializer<Bits256> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Bits256", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Bits256 =
        Bits256(decoder.decodeString().decodeBase64Bytes())

    override fun serialize(encoder: Encoder, value: Bits256): Unit =
        encoder.encodeString(value.value.toByteArray().encodeBase64())
}
