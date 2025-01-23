package org.ton.block.message.inmsg

import org.ton.block.currency.Coins
import org.ton.block.message.envelope.MsgEnvelope
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class MsgDiscardFinal(
    val inMsg: CellRef<MsgEnvelope>,
    val transactionId: ULong,
    val fwdFee: Coins
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_discard_fin") {
            field("in_msg", inMsg)
            field("transaction_id", transactionId)
            field("fwd_fee", fwdFee)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<MsgDiscardFinal> by MsgDiscardFinTlbConstructor
}

private object MsgDiscardFinTlbConstructor : TlbConstructor<MsgDiscardFinal>(
    schema = "msg_discard_fin\$110 in_msg:^MsgEnvelope transaction_id:uint64 fwd_fee:Coins = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgDiscardFinal
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeUInt64(value.transactionId)
        storeTlb(Coins.Tlb, value.fwdFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgDiscardFinal = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val transactionId = loadULong()
        val fwdFee = loadTlb(Coins.Tlb)
        MsgDiscardFinal(inMsg, transactionId, fwdFee)
    }
}
