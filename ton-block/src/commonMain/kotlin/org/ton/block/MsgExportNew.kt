package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_new")
data class MsgExportNew(
    val out_msg: MsgEnvelope,
    val transaction: Transaction
) : OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgExportNew> = MsgExportNewTlbConstructor
    }
}

private object MsgExportNewTlbConstructor : TlbConstructor<MsgExportNew>(
    schema = "msg_export_new\$001 out_msg:^MsgEnvelope transaction:^Transaction = OutMsg;"
) {
    val msgEnvelope by lazy { MsgEnvelope.tlbCodec() }
    val transaction by lazy { Transaction.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportNew
    ) = cellBuilder {
        storeRef { storeTlb(msgEnvelope, value.out_msg) }
        storeRef { storeTlb(transaction, value.transaction) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportNew = cellSlice {
        val outMsg = loadRef { loadTlb(msgEnvelope) }
        val transaction = loadRef { loadTlb(transaction) }
        MsgExportNew(outMsg, transaction)
    }
}