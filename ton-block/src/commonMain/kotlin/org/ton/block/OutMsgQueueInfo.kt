package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.AugDictionary
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class OutMsgQueueInfo(
    val out_queue: AugDictionary<EnqueuedMsg, Long>,
    val proc_info: HashMapE<ProcessedUpto>,
    val ihr_pending: HashMapE<IhrPendingSince>
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<OutMsgQueueInfo> = OutMsgQueueInfoTlbConstructor
    }
}

private object OutMsgQueueInfoTlbConstructor : TlbConstructor<OutMsgQueueInfo>(
    schema = "_ out_queue:OutMsgQueue proc_info:ProcessedInfo ihr_pending:IhrPendingInfo = OutMsgQueueInfo;"
) {
    val outQueue by lazy { AugDictionary.tlbCodec(352, EnqueuedMsg.tlbCodec(), UIntTlbConstructor.long(64)) }
    val procInfo by lazy { HashMapE.tlbCodec(96, ProcessedUpto.tlbCodec()) }
    val ihrPending by lazy { HashMapE.tlbCodec(320, IhrPendingSince.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgQueueInfo
    ) = cellBuilder {
        storeTlb(outQueue, value.out_queue)
        storeTlb(procInfo, value.proc_info)
        storeTlb(ihrPending, value.ihr_pending)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgQueueInfo = cellSlice {
        val outQueue = loadTlb(outQueue)
        val procInfo = loadTlb(procInfo)
        val ihrPending = loadTlb(ihrPending)
        OutMsgQueueInfo(outQueue, procInfo, ihrPending)
    }
}