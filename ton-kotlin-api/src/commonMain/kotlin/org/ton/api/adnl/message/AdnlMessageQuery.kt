package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.bitstring.BitString
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Polymorphic
@SerialName("adnl.message.query")
@Serializable
data class AdnlMessageQuery(
    val query_id: BitString,
    @Serializable(HexByteArraySerializer::class)
    val query: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageQuery) return false
        if (query_id != other.query_id) return false
        if (!query.contentEquals(other.query)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = query_id.hashCode()
        result = 31 * result + query.contentHashCode()
        return result
    }

    companion object : TlConstructor<AdnlMessageQuery>(
        type = AdnlMessageQuery::class,
        schema = "adnl.message.query query_id:int256 query:bytes = adnl.Message",
        fields = listOf(Int256TlConstructor, BytesTlConstructor)
    ) {
        fun sizeOf(value: AdnlMessageQuery): Int =
            Int256TlConstructor.SIZE_BYTES + BytesTlConstructor.sizeOf(value.query)

        override fun encode(output: Output, value: AdnlMessageQuery) {
            output.writeInt256Tl(value.query_id.toByteArray())
            output.writeBytesTl(value.query)
        }

        override fun decode(values: Iterator<*>): AdnlMessageQuery {
            val queryId = values.next() as ByteArray
            val query = values.next() as ByteArray
            return AdnlMessageQuery(BitString(queryId), query)
        }
    }
}
