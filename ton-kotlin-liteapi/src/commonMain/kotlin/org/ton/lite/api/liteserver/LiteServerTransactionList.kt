package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.hex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeVectorTl

data class LiteServerTransactionList(
    val ids: List<TonNodeBlockIdExt>,
    val transactions: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerTransactionList) return false
        if (ids != other.ids) return false
        if (!transactions.contentEquals(other.transactions)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = ids.hashCode()
        result = 31 * result + transactions.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("(")
        append("ids:")
        append(ids)
        append(" transactions:")
        append(hex(transactions).uppercase())
        append(")")
    }

    companion object : TlCodec<LiteServerTransactionList> by LiteServerTransactionListTlConstructor
}

private object LiteServerTransactionListTlConstructor : TlConstructor<LiteServerTransactionList>(
    schema = "liteServer.transactionList ids:(vector tonNode.blockIdExt) transactions:bytes = liteServer.TransactionList",
    id = 1864812043
) {
    override fun decode(input: Input): LiteServerTransactionList {
        val ids = input.readVectorTl(TonNodeBlockIdExt)
        val transactions = input.readBytesTl()
        return LiteServerTransactionList(ids, transactions)
    }

    override fun encode(output: Output, value: LiteServerTransactionList) {
        output.writeVectorTl(value.ids, TonNodeBlockIdExt)
        output.writeBytesTl(value.transactions)
    }
}
