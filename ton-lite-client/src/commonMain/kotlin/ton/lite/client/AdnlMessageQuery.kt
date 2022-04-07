package ton.lite.client

import io.ktor.utils.io.core.*

data class AdnlMessageQuery(
    val queryId: LongArray,
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

    companion object : TLCodec<AdnlMessageQuery> {
        override val id = -1265895046

        override fun encode(output: Output, message: AdnlMessageQuery) {
            output.writeFully(message.queryId)
            output.writeByteArray(message.query)
        }

        override fun decode(input: Input): AdnlMessageQuery {
            val queryId = LongArray(4).also { input.readFully(it) }
            val query = input.readByteArray()
            return AdnlMessageQuery(queryId, query)
        }
    }
}