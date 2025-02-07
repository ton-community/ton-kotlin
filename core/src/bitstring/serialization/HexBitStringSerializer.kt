package org.ton.kotlin.bitstring.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ton.kotlin.bitstring.BitString

public object HexBitStringSerializer : KSerializer<BitString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BitString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BitString {
        val hex = decoder.decodeString()
        return BitString(hex)
    }

    override fun serialize(encoder: Encoder, value: BitString) {
        encoder.encodeString(value.toString())
    }
}
