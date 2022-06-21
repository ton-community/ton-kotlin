package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_deq")
data class MsgExportDeq(
    val out_msg: MsgEnvelope,
    val import_block_lt: Long
) : OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgExportDeq> = MsgExportDeqTlbConstructor
    }
}

private object MsgExportDeqTlbConstructor : TlbConstructor<MsgExportDeq>(
    schema = "msg_export_deq\$1100 out_msg:^MsgEnvelope import_block_lt:uint63 = OutMsg;"
) {
    val msgEnvelope by lazy { MsgEnvelope.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportDeq
    ) = cellBuilder {
        storeRef { storeTlb(msgEnvelope, value.out_msg) }
        storeUInt(value.import_block_lt, 63)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportDeq = cellSlice {
        val outMsg = loadRef { loadTlb(msgEnvelope) }
        val importBlockLt = loadUInt(63).toLong()
        MsgExportDeq(outMsg, importBlockLt)
    }
}