package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
data class EnqueuedMsg(
    val enqueued_lt: ULong,
    val out_msg: MsgEnvelope
) {
    companion object : TlbConstructorProvider<EnqueuedMsg> by EnqueuedMsgTlbConstructor
}

private object EnqueuedMsgTlbConstructor : TlbConstructor<EnqueuedMsg>(
    schema = "_ enqueued_lt:uint64 out_msg:^MsgEnvelope = EnqueuedMsg;\n"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: EnqueuedMsg
    ) = cellBuilder {
        storeUInt64(value.enqueued_lt)
        storeRef {
            storeTlb(MsgEnvelope, value.out_msg)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): EnqueuedMsg = cellSlice {
        val enqueuedLt = loadUInt64()
        val outMsg = loadRef {
            loadTlb(MsgEnvelope)
        }
        EnqueuedMsg(enqueuedLt, outMsg)
    }
}
