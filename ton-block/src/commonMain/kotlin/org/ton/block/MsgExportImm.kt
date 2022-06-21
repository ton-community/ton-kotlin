package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_imm")
data class MsgExportImm(
    val out_msg: MsgEnvelope,
    val transaction: Transaction,
    val reimport: InMsg
) : OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgExportImm> = MsgExportImmTlbConstructor
    }
}

private object MsgExportImmTlbConstructor : TlbConstructor<MsgExportImm>(
    schema = "msg_export_imm\$010 out_msg:^MsgEnvelope transaction:^Transaction reimport:^InMsg = OutMsg;"
) {
    val msgEnvelope by lazy { MsgEnvelope.tlbCodec() }
    val transaction by lazy { Transaction.tlbCodec() }
    val inMsg by lazy { InMsg.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportImm
    ) = cellBuilder {
        storeRef { storeTlb(msgEnvelope, value.out_msg) }
        storeRef { storeTlb(transaction, value.transaction) }
        storeRef { storeTlb(inMsg, value.reimport) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportImm = cellSlice {
        val outMsg = loadRef { loadTlb(msgEnvelope) }
        val transaction = loadRef { loadTlb(transaction) }
        val reimport = loadRef { loadTlb(inMsg) }
        MsgExportImm(outMsg, transaction, reimport)
    }
}