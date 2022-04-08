package ton.adnl.message

import io.ktor.utils.io.core.*
import ton.adnl.TLCodec
import ton.crypto.hex

data class AdnlMessageQuery(
    val queryId: ByteArray,
    val query: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageQuery

        if (!queryId.contentEquals(other.queryId)) return false
        if (!query.contentEquals(other.query)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.contentHashCode()
        result = 31 * result + query.contentHashCode()
        return result
    }

    override fun toString() = "AdnlMessageQuery(queryId=${hex(queryId)}, query=${hex(query)})"

    companion object : TLCodec<AdnlMessageQuery> {
        override val id = -1265895046

        override fun encode(output: Output, message: AdnlMessageQuery) {
            output.writeFully(message.queryId)
            output.writeByteArray(message.query)
        }

        override fun decode(input: Input): AdnlMessageQuery {
            val queryId = input.readBytes(32)
            val query = input.readByteArray()
            return AdnlMessageQuery(queryId, query)
        }
    }
}