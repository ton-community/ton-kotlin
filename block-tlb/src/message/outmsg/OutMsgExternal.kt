package org.ton.block.message.export

import org.ton.block.message.Message
import org.ton.block.transaction.Transaction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgExternal(
    val msg: CellRef<Message<Cell>>,
    val transaction: CellRef<Transaction>
) : OutMsg {
    public companion object : TlbConstructorProvider<OutMsgExternal> by MsgExportExtTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_ext") {
            field("msg", msg)
            field("transaction", transaction)
        }
    }
}

private object MsgExportExtTlbConstructor : TlbConstructor<OutMsgExternal>(
    schema = "msg_export_ext\$000 msg:^(Message Any) transaction:^Transaction = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgExternal
    ) = cellBuilder {
        storeRef(Message.Companion.Any, value.msg)
        storeRef(Transaction, value.transaction)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgExternal = cellSlice {
        val msg = loadRef(Message.Companion.Any)
        val transaction = loadRef(Transaction)
        OutMsgExternal(msg, transaction)
    }
}
