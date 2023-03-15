package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.base64
import org.ton.tl.*

@Serializable
@SerialName("liteServer.transactionList")
public data class LiteServerTransactionList(
    val ids: List<TonNodeBlockIdExt>,
    val transactions: String
) {
    public companion object : TlCodec<LiteServerTransactionList> by LiteServerTransactionListTlConstructor
}

private object LiteServerTransactionListTlConstructor : TlConstructor<LiteServerTransactionList>(
    schema = "liteServer.transactionList ids:(vector tonNode.blockIdExt) transactions:bytes = liteServer.TransactionList",
) {
    override fun decode(reader: TlReader): LiteServerTransactionList {
        val ids = reader.readVector {
            read(TonNodeBlockIdExt)
        }
        val transactions = base64(reader.readBytes())
        return LiteServerTransactionList(ids, transactions)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionList) {
        writer.writeVector(value.ids) {
            write(TonNodeBlockIdExt, it)
        }
        writer.writeBytes(base64(value.transactions))
    }
}
