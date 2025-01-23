package org.ton.block.message.inmsg

import org.ton.block.currency.Coins
import org.ton.block.message.Message
import org.ton.block.transaction.Transaction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class InMsgIhr(
    val msg: CellRef<Message<Cell>>,
    val transaction: CellRef<Transaction>,
    val ihrFee: Coins,
    val proofCreated: Cell
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_import_ihr") {
            printer.field("msg", msg)
            printer.field("transaction", transaction)
            printer.field("ihr_fee", ihrFee)
            printer.field("proof_created", proofCreated)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<InMsgIhr> by MsgImportIhrTlbConstructor
}

private object MsgImportIhrTlbConstructor : TlbConstructor<InMsgIhr>(
    schema = "msg_import_ihr\$010 msg:^(Message Any) transaction:^Transaction ihr_fee:Grams proof_created:^Cell = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: InMsgIhr
    ) = cellBuilder {
        storeRef(Message.Companion.Any, value.msg)
        storeRef(Transaction, value.transaction)
        storeTlb(Coins.Tlb, value.ihrFee)
        storeRef(value.proofCreated)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): InMsgIhr = cellSlice {
        val msg = loadRef(Message.Companion.Any)
        val transaction = loadRef(Transaction)
        val ihrFee = loadTlb(Coins.Tlb)
        val proofCreated = loadRef()
        InMsgIhr(msg, transaction, ihrFee, proofCreated)
    }
}
