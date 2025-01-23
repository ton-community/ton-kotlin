package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.message.inmsg.InMsg
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgDequeImmediate(
    val outMsg: CellRef<MsgEnvelope>,
    val reimport: CellRef<InMsg>,
) : OutMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_deq_imm") {
            field("out_msg")
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<OutMsgDequeImmediate> by MsgExportDeqImmTlbConstructor
}

private object MsgExportDeqImmTlbConstructor : TlbConstructor<OutMsgDequeImmediate>(
    schema = "msg_export_deq_imm\$100 out_msg:^MsgEnvelope reimport:^InMsg = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgDequeImmediate
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(InMsg.Companion, value.reimport)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgDequeImmediate = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val reimport = loadRef(InMsg.Companion)
        OutMsgDequeImmediate(outMsg, reimport)
    }
}
