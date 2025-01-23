package org.ton.block.message.inmsg

import org.ton.block.currency.Coins
import org.ton.block.message.envelope.MsgEnvelope
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class InMsgTransit(
    val inMsg: CellRef<MsgEnvelope>,
    val outMsg: CellRef<MsgEnvelope>,
    val transitFee: Coins
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_import_tr") {
            field("in_msg", inMsg)
            field("out_msg", outMsg)
            field("transit_fee", transitFee)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<InMsgTransit> by MsgImportTrTlbConstructor
}

private object MsgImportTrTlbConstructor : TlbConstructor<InMsgTransit>(
    schema = "msg_import_tr\$101  in_msg:^MsgEnvelope out_msg:^MsgEnvelope transit_fee:Coins = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: InMsgTransit
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeRef(MsgEnvelope, value.outMsg)
        storeTlb(Coins.Tlb, value.transitFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): InMsgTransit = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val outMsg = loadRef(MsgEnvelope)
        val transitFee = loadTlb(Coins.Tlb)
        InMsgTransit(inMsg, outMsg, transitFee)
    }
}
