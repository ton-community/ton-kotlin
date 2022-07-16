package org.ton.adnl.node

import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.tl.TlCodec
import org.ton.tl.TlObject

data class QueryId(
    val value: ByteArray = ByteArray(32)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QueryId) return false
        if (!value.contentEquals(other.value)) return false
        return true
    }

    override fun hashCode(): Int = value.contentHashCode()
}

class Query<Q : TlObject<Q>, A : TlObject<A>>(
    val id: QueryId,
    val query: Q,
    val answerCodec: TlCodec<A>
) {
    constructor(query: Q, answerCodec: TlCodec<A>) : this(QueryId(), query, answerCodec)

    lateinit var answer: A

    fun createMessageQuery(): AdnlMessageQuery =
        AdnlMessageQuery(id.value, query.tlCodec().encodeBoxed(query))
}