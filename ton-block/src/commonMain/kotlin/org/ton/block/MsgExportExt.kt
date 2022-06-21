package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
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
    val messageAny by lazy { Message.tlbCodec(Cell.tlbCodec()) }
    val transaction by lazy { Transaction.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgExportExt
    ) = cellBuilder {
        storeRef { storeTlb(messageAny, value.msg) }
        storeRef { storeTlb(transaction, value.transaction) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgExportExt = cellSlice {
        val msg = loadRef { loadTlb(messageAny) }
        val transaction = loadRef { loadTlb(transaction) }
        MsgExportExt(msg, transaction)
    }
}