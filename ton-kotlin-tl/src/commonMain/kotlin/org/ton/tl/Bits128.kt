package org.ton.tl

import io.ktor.util.*
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
@Serializable(with = Bits128.KSerializer::class)
public value class Bits128(
    public val value: BitString
) {
    public constructor(value: ByteArray) : this(value.toBitString())

    init {
        require(value.size == 128) { "Bits128 must be 128 bits long" }
    }

    public inline fun toByteArray(): ByteArray = value.toByteArray()
    public inline fun toBitString(): BitString = value

    public object KSerializer : kotlinx.serialization.KSerializer<Bits128> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Bits256", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Bits128 =
            Bits128(decoder.decodeString().decodeBase64Bytes())

        override fun serialize(encoder: Encoder, value: Bits128): Unit =
            encoder.encodeString(value.value.toByteArray().encodeBase64())
    }
}
