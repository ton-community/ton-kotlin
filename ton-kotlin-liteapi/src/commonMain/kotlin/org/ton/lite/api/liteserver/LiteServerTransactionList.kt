package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.transactionList")
public data class LiteServerTransactionList(
    val ids: Collection<TonNodeBlockIdExt>,
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

    public companion object : TlCodec<LiteServerTransactionList> by LiteServerTransactionListTlConstructor
}

private object LiteServerTransactionListTlConstructor : TlConstructor<LiteServerTransactionList>(
    schema = "liteServer.transactionList ids:(vector tonNode.blockIdExt) transactions:bytes = liteServer.TransactionList",
) {
    override fun decode(reader: TlReader): LiteServerTransactionList {
        val ids = reader.readVector {
            read(TonNodeBlockIdExt)
        }
        val transactions = reader.readBytes()
        return LiteServerTransactionList(ids, transactions)
    }

    override fun encode(output: TlWriter, value: LiteServerTransactionList) {
        output.writeVector(value.ids) {
            write(TonNodeBlockIdExt, it)
        }
        output.writeBytes(value.transactions)
    }
}
