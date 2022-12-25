package org.ton.lite.api.liteserver

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

public data class LiteServerTransactionList(
    val ids: Collection<TonNodeBlockIdExt>,
    val transactions: BagOfCells
) {
    public companion object : TlCodec<LiteServerTransactionList> by LiteServerTransactionListTlConstructor
}

private object LiteServerTransactionListTlConstructor : TlConstructor<LiteServerTransactionList>(
    schema = "liteServer.transactionList ids:(vector tonNode.blockIdExt) transactions:bytes = liteServer.TransactionList",
) {
    override fun decode(reader: TlReader): LiteServerTransactionList {
        val ids = reader.readCollection {
            read(TonNodeBlockIdExt)
        }
        val transactions = reader.readBoc()
        return LiteServerTransactionList(ids, transactions)
    }

    override fun encode(output: TlWriter, value: LiteServerTransactionList) {
        output.writeCollection(value.ids) {
            write(TonNodeBlockIdExt, it)
        }
        output.writeBoc(value.transactions)
    }
}
