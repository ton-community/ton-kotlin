package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.message.inmsg.InMsg
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgTransit(
    val outMsg: CellRef<MsgEnvelope>,
    val imported: CellRef<InMsg>
) : OutMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_tr") {
            field("out_msg", outMsg)
            field("imported", imported)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<OutMsgTransit> by MsgExportTrTlbConstructor
}

private object MsgExportTrTlbConstructor : TlbConstructor<OutMsgTransit>(
    schema = "msg_export_tr\$011 out_msg:^MsgEnvelope imported:^InMsg = OutMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgTransit
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(InMsg.Companion, value.imported)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgTransit = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val imported = loadRef(InMsg.Companion)
        OutMsgTransit(outMsg, imported)
    }
}
