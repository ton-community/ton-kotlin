package org.ton.api.rldp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
@SerialName("rldp.message")
public data class RldpMessageData(
    override val id: Bits256,
    override val data: ByteArray
) : RldpMessage {

    override fun tlCodec(): TlCodec<RldpMessageData> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpMessageData) return false

        if (id != other.id) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    public companion object : TlConstructor<RldpMessageData>(
        schema = "rldp.message id:int256 data:bytes = rldp.Message",
    ) {
        override fun encode(output: TlWriter, value: RldpMessageData) {
            output.writeBits256(value.id)
            output.writeBytes(value.data)
        }

        override fun decode(input: TlReader): RldpMessageData {
            val id = input.readBits256()
            val data = input.readBytes()
            return RldpMessageData(id, data)
        }
    }
}
