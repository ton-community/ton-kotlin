package org.ton.bigint.serialization

import org.ton.bigint.BigInt
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object BigIntSerializer : KSerializer<BigInt> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("org.ton.tonkt.BigInt", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: BigInt
    ) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): BigInt {
        return BigInt(decoder.decodeString())
    }
}