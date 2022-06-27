package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_tr")
data class MsgExportTr(
    val out_msg: MsgEnvelope,
    val imported: InMsg
) : OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgExportTr> = MsgExportTrTlbConstructor
    }
}

private object MsgExportTrTlbConstructor : TlbConstructor<MsgExportTr>(
    schema = "msg_export_tr\$011 out_msg:^MsgEnvelope imported:^InMsg = OutMsg;"
) {
    val inMsg by lazy { InMsg.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportTr
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeRef { storeTlb(inMsg, value.imported) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportTr = cellSlice {
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val imported = loadRef { loadTlb(inMsg) }
        MsgExportTr(outMsg, imported)
    }
}