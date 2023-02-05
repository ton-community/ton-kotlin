package org.ton.api.rldp

import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.tl.*

@SerialName("rldp.answer")
@Serializable
public data class RldpAnswer(
    @SerialName("query_id")
    val queryId: Bits256,
    override val data: ByteArray
) : RldpMessage {
    override val id: Bits256
        get() = queryId

    override fun tlCodec(): TlCodec<RldpAnswer> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpAnswer) return false

        if (queryId != other.queryId) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String =
        "RldpAnswer(query_id=$queryId, data=[(${data.size} bytes) ${data.encodeBase64()}])"

    public companion object : TlConstructor<RldpAnswer>(
        schema = "rldp.answer query_id:int256 data:bytes = rldp.Message",
    ) {
        override fun encode(writer: TlWriter, value: RldpAnswer) {
            writer.writeBits256(value.queryId)
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): RldpAnswer {
            val query_id = reader.readBits256()
            val data = reader.readBytes()
            return RldpAnswer(query_id, data)
        }
    }
}
