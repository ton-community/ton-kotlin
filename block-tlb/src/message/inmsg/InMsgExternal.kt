@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block.message.inmsg

import org.ton.block.message.Message
import org.ton.block.transaction.Transaction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class InMsgExternal(
    val msg: CellRef<Message<Cell>>,
    val transaction: CellRef<Transaction>
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("msg_import_ext") {
        field("msg", msg)
        field("transaction", transaction)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<InMsgExternal> by MsgImportExtTlbConstructor
}

private object MsgImportExtTlbConstructor : TlbConstructor<InMsgExternal>(
    schema = "msg_import_ext\$000 msg:^(Message Any) transaction:^Transaction = InMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: InMsgExternal
    ) = cellBuilder {
        storeRef(Message.Companion.Any, value.msg)
        storeRef(Transaction, value.transaction)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): InMsgExternal = cellSlice {
        val msg = loadRef(Message.Companion.Any)
        val transaction = loadRef(Transaction)
        InMsgExternal(msg, transaction)
    }
}
