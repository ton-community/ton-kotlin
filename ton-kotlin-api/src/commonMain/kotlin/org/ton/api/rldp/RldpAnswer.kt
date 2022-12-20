package org.ton.api.rldp

import io.ktor.util.*
import org.ton.bitstring.BitString
import org.ton.tl.*

public data class RldpAnswer(
    val query_id: Bits256,
    override val data: ByteArray
) : RldpMessage {
    override val id: Bits256
        get() = query_id

    override fun tlCodec(): TlCodec<RldpAnswer> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpAnswer) return false

        if (query_id != other.query_id) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = query_id.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String =
        "RldpAnswer(query_id=$query_id, data=[(${data.size} bytes) ${data.encodeBase64()}])"

    public companion object : TlConstructor<RldpAnswer>(
        schema = "rldp.answer query_id:int256 data:bytes = rldp.Message",
    ) {
        override fun encode(writer: TlWriter, value: RldpAnswer) {
            writer.writeBits256(value.query_id)
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): RldpAnswer {
            val query_id = reader.readBits256()
            val data = reader.readBytes()
            return RldpAnswer(query_id, data)
        }
    }
}
