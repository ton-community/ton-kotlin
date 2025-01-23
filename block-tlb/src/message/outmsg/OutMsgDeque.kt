package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgDeque(
    val outMsg: CellRef<MsgEnvelope>,
    val importBlockLt: ULong
) : OutMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_deq") {
            field("out_msg", outMsg)
            field("import_block_lt", importBlockLt)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<OutMsgDeque> by MsgExportDeqTlbConstructor
}

private object MsgExportDeqTlbConstructor : TlbConstructor<OutMsgDeque>(
    schema = "msg_export_deq\$1100 out_msg:^MsgEnvelope import_block_lt:uint63 = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgDeque
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeUInt(value.importBlockLt.toLong(), 63)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgDeque = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val importBlockLt = loadULong(63)
        OutMsgDeque(outMsg, importBlockLt)
    }
}
