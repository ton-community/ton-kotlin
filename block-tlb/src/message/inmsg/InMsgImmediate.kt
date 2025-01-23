package org.ton.block.message.inmsg

import kotlinx.serialization.SerialName
import org.ton.block.currency.Coins
import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.transaction.Transaction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class InMsgImmediate(
    @SerialName("in_msg") val inMsg: CellRef<MsgEnvelope>,
    val transaction: CellRef<Transaction>,
    @SerialName("fwd_fee") val fwdFee: Coins
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("msg_import_imm") {
        field("in_msg", inMsg)
        field("transaction", transaction)
        field("fwd_fee", fwdFee)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<InMsgImmediate> by MsgImportImmTlbConstructor
}

private object MsgImportImmTlbConstructor : TlbConstructor<InMsgImmediate>(
    schema = "msg_import_imm\$011 in_msg:^MsgEnvelope transaction:^Transaction fwd_fee:Coins = InMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: InMsgImmediate
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeRef(Transaction, value.transaction)
        storeTlb(Coins.Tlb, value.fwdFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): InMsgImmediate = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val transaction = loadRef(Transaction)
        val fwdFee = loadTlb(Coins.Tlb)
        InMsgImmediate(inMsg, transaction, fwdFee)
    }
}
