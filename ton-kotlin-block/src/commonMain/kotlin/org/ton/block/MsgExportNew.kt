package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_new")
data class MsgExportNew(
    val out_msg: MsgEnvelope,
    val transaction: Transaction
) : OutMsg {
    companion object : TlbConstructorProvider<MsgExportNew> by MsgExportNewTlbConstructor
}

private object MsgExportNewTlbConstructor : TlbConstructor<MsgExportNew>(
    schema = "msg_export_new\$001 out_msg:^MsgEnvelope transaction:^Transaction = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportNew
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeRef { storeTlb(Transaction, value.transaction) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportNew = cellSlice {
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val transaction = loadRef { loadTlb(Transaction) }
        MsgExportNew(outMsg, transaction)
    }
}
