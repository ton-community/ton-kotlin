package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.message")
data class RldpMessageData(
    override val id: BitString,
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

    companion object : TlConstructor<RldpMessageData>(
        schema = "rldp.message id:int256 data:bytes = rldp.Message",
    ) {
        override fun encode(output: Output, value: RldpMessageData) {
            output.writeInt256Tl(value.id)
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): RldpMessageData {
            val id = input.readInt256Tl()
            val data = input.readBytesTl()
            return RldpMessageData(BitString(id), data)
        }
    }
}
