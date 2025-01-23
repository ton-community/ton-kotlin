package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.message.inmsg.InMsg
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgTransitRequest(
    val outMsg: CellRef<MsgEnvelope>,
    val imported: CellRef<InMsg>,
) : OutMsg {
    public companion object : TlbConstructorProvider<OutMsgTransitRequest> by MsgExportTrReqTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_tr_req") {
            field("out_msg", outMsg)
            field("imported", imported)
        }
    }

    override fun toString(): String = print().toString()
}

private object MsgExportTrReqTlbConstructor : TlbConstructor<OutMsgTransitRequest>(
    schema = "msg_export_tr_req\$111 out_msg:^MsgEnvelope imported:^InMsg = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgTransitRequest
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(InMsg.Companion, value.imported)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgTransitRequest = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val imported = loadRef(InMsg.Companion)
        OutMsgTransitRequest(outMsg, imported)
    }
}
