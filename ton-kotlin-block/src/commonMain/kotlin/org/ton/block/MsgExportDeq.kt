package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_deq")
data class MsgExportDeq(
    val out_msg: MsgEnvelope,
    val import_block_lt: ULong
) : OutMsg {
    companion object : TlbConstructorProvider<MsgExportDeq> by MsgExportDeqTlbConstructor
}

private object MsgExportDeqTlbConstructor : TlbConstructor<MsgExportDeq>(
    schema = "msg_export_deq\$1100 out_msg:^MsgEnvelope import_block_lt:uint63 = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportDeq
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeUInt(value.import_block_lt.toLong(), 63)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportDeq = cellSlice {
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val importBlockLt = loadUInt(63).toLong().toULong()
        MsgExportDeq(outMsg, importBlockLt)
    }
}
