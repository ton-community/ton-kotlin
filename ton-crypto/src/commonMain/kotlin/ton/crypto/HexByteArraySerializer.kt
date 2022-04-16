package ton.crypto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object HexByteArraySerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = serialDescriptor<ByteArray>()

    override fun deserialize(decoder: Decoder): ByteArray = hex(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(hex(value))
    }
}