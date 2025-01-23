package org.ton.block.message.inmsg

import org.ton.block.currency.Coins
import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.transaction.Transaction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class InMsgFinal(
    val inMsg: CellRef<MsgEnvelope>,
    val transaction: CellRef<Transaction>,
    val fwdFee: Coins
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_import_fin") {
            field("in_msg", inMsg)
            field("transaction", transaction)
            field("fwd_fee", fwdFee)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<InMsgFinal> by MsgImportFinTlbConstructor
}

private object MsgImportFinTlbConstructor : TlbConstructor<InMsgFinal>(
    schema = "msg_import_fin\$100 in_msg:^MsgEnvelope transaction:^Transaction fwd_fee:Coins = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: InMsgFinal
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeRef(Transaction, value.transaction)
        storeTlb(Coins.Tlb, value.fwdFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): InMsgFinal = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val transaction = loadRef(Transaction)
        val fwdFee = loadTlb(Coins.Tlb)
        InMsgFinal(inMsg, transaction, fwdFee)
    }
}
