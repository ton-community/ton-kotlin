package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("msg_export_deq_imm")
public data class MsgExportDeqImm(
    val outMsg: CellRef<MsgEnvelope>,
    val reimport: CellRef<InMsg>,
) : OutMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_deq_imm") {
            field("out_msg")
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<MsgExportDeqImm> by MsgExportDeqImmTlbConstructor
}

private object MsgExportDeqImmTlbConstructor : TlbConstructor<MsgExportDeqImm>(
    schema = "msg_export_deq_imm\$100 out_msg:^MsgEnvelope reimport:^InMsg = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportDeqImm
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(InMsg, value.reimport)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportDeqImm = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val reimport = loadRef(InMsg)
        MsgExportDeqImm(outMsg, reimport)
    }
}
