package org.ton.api.adnl.message

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.BytesTlConstructor

@Polymorphic
@SerialName("adnl.message.query")
@Serializable
public data class AdnlMessageQuery(
    @SerialName("query_id")
    val queryId: Bits256,
    val query: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageQuery) return false
        if (queryId != other.queryId) return false
        if (!query.contentEquals(other.query)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = queryId.hashCode()
        result = 31 * result + query.contentHashCode()
        return result
    }

    public companion object : TlConstructor<AdnlMessageQuery>(
        schema = "adnl.message.query query_id:int256 query:bytes = adnl.Message",
    ) {
        public fun sizeOf(value: AdnlMessageQuery): Int =
            256 / Byte.SIZE_BYTES + BytesTlConstructor.sizeOf(value.query)

        override fun encode(output: TlWriter, value: AdnlMessageQuery) {
            output.writeBits256(value.queryId)
            output.writeBytes(value.query)
        }

        override fun decode(input: TlReader): AdnlMessageQuery {
            val queryId = input.readBits256()
            val query = input.readBytes()
            return AdnlMessageQuery(queryId, query)
        }
    }
}
