package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_export_ext")
data class MsgExportExt(
    val msg: Message<Cell>,
    val transaction: Transaction
) : OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgExportExt> = MsgExportExtTlbConstructor
    }
}

private object MsgExportExtTlbConstructor : TlbConstructor<MsgExportExt>(
    schema = "msg_export_ext\$000 msg:^(Message Any) transaction:^Transaction = OutMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportExt
    ) = cellBuilder {
        storeRef { storeTlb(Message.Any, value.msg) }
        storeRef { storeTlb(Transaction, value.transaction) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportExt = cellSlice {
        val msg = loadRef { loadTlb(Message.Any) }
        val transaction = loadRef { loadTlb(Transaction) }
        MsgExportExt(msg, transaction)
    }
}