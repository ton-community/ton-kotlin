package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_imm")
data class MsgExportImm(
    val out_msg: MsgEnvelope,
    val transaction: Transaction,
    val reimport: InMsg
) : OutMsg {
    companion object : TlbConstructorProvider<MsgExportImm> by MsgExportImmTlbConstructor
}

private object MsgExportImmTlbConstructor : TlbConstructor<MsgExportImm>(
    schema = "msg_export_imm\$010 out_msg:^MsgEnvelope transaction:^Transaction reimport:^InMsg = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportImm
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeRef { storeTlb(Transaction, value.transaction) }
        storeRef { storeTlb(InMsg, value.reimport) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportImm = cellSlice {
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val transaction = loadRef { loadTlb(Transaction) }
        val reimport = loadRef { loadTlb(InMsg) }
        MsgExportImm(outMsg, transaction, reimport)
    }
}
