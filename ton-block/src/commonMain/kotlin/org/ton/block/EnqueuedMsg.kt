package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class EnqueuedMsg(
    val enqueued_lt: Long,
    val out_msg: MsgEnvelope
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<EnqueuedMsg> = EnqueuedMsgTlbConstructor
    }
}

private object EnqueuedMsgTlbConstructor : TlbConstructor<EnqueuedMsg>(
    schema = "_ enqueued_lt:uint64 out_msg:^MsgEnvelope = EnqueuedMsg;\n"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: EnqueuedMsg
    ) = cellBuilder {
        storeUInt(value.enqueued_lt, 64)
        storeRef {
            storeTlb(MsgEnvelope, value.out_msg)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): EnqueuedMsg = cellSlice {
        val enqueuedLt = loadUInt(64).toLong()
        val outMsg = loadRef {
            loadTlb(MsgEnvelope)
        }
        EnqueuedMsg(enqueuedLt, outMsg)
    }
}