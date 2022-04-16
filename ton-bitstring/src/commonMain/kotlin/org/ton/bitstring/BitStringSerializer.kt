@file:OptIn(ExperimentalUnsignedTypes::class, ExperimentalSerializationApi::class)

package org.ton.bitstring

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BitStringSerializer : KSerializer<BitString> {
    private val byteArraySerializer = ByteArraySerializer()
    override val descriptor: SerialDescriptor = SerialDescriptor("BitString", byteArraySerializer.descriptor)

    override fun serialize(encoder: Encoder, value: BitString) {
        encoder.encodeSerializableValue(byteArraySerializer, value.bits.toByteArray())
    }

    override fun deserialize(decoder: Decoder): BitString {
        val array = decoder.decodeSerializableValue(byteArraySerializer).toUByteArray()
        return BitString(array.size * 8).also {
            array.copyInto(it.bits)
        }
    }
}