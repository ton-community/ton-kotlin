package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_tr_req")
data class MsgExportTrReq(
    val out_msg: MsgEnvelope,
    val imported: InMsg,
) : OutMsg {
    companion object : TlbConstructorProvider<MsgExportTrReq> by MsgExportTrReqTlbConstructor
}

private object MsgExportTrReqTlbConstructor : TlbConstructor<MsgExportTrReq>(
    schema = "msg_export_tr_req\$111 out_msg:^MsgEnvelope imported:^InMsg = OutMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportTrReq
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeRef { storeTlb(InMsg, value.imported) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportTrReq = cellSlice {
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val imported = loadRef { loadTlb(InMsg) }
        MsgExportTrReq(outMsg, imported)
    }
}
