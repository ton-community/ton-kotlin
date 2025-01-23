package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.transaction.Transaction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgNew(
    val outMsg: CellRef<MsgEnvelope>,
    val transaction: CellRef<Transaction>
) : OutMsg, TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_new") {
            field("out_msg", outMsg)
            field("transaction", transaction)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<OutMsgNew> by MsgExportNewTlbConstructor
}

private object MsgExportNewTlbConstructor : TlbConstructor<OutMsgNew>(
    schema = "msg_export_new\$001 out_msg:^MsgEnvelope transaction:^Transaction = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgNew
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(Transaction, value.transaction)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgNew = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val transaction = loadRef(Transaction)
        OutMsgNew(outMsg, transaction)
    }
}
