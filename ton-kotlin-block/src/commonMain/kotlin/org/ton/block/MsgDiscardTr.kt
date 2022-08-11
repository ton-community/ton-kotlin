package org.ton.block

import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

data class MsgDiscardTr(
    val in_msg: MsgEnvelope,
    val transaction_id: Long,
    val fwd_fee: Coins,
    val proof_delivered: Cell
) : InMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgDiscardTr> = MsgDiscardTrTlbConstructor
    }
}

private object MsgDiscardTrTlbConstructor : TlbConstructor<MsgDiscardTr>(
    schema = "msg_discard_tr\$111 in_msg:^MsgEnvelope transaction_id:uint64 fwd_fee:Coins proof_delivered:^Cell = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgDiscardTr
    ) = cellBuilder {
        storeRef {
            storeTlb(MsgEnvelope, value.in_msg)
        }
        storeUInt(value.transaction_id, 64)
        storeTlb(Coins, value.fwd_fee)
        storeRef(value.proof_delivered)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgDiscardTr = cellSlice {
        val inMsg = loadRef {
            loadTlb(MsgEnvelope)
        }
        val transactionId = loadUInt(64).toLong()
        val fwdFee = loadTlb(Coins)
        val proofDelivered = loadRef()
        MsgDiscardTr(inMsg, transactionId, fwdFee, proofDelivered)
    }
}
