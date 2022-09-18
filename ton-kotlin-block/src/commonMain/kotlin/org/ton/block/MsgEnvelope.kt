package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_envelope")
data class MsgEnvelope(
    val cur_addr: IntermediateAddress,
    val next_addr: IntermediateAddress,
    val fwd_fee_remaining: Coins,
    val msg: Message<Cell>
) {
    companion object : TlbCodec<MsgEnvelope> by MsgEnvelopeTlbConstructor.asTlbCombinator()
}

private object MsgEnvelopeTlbConstructor : TlbConstructor<MsgEnvelope>(
    schema = "msg_envelope#4 cur_addr:IntermediateAddress " +
        "next_addr:IntermediateAddress fwd_fee_remaining:Coins " +
        "msg:^(Message Any) = MsgEnvelope;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgEnvelope
    ) = cellBuilder {
        storeTlb(IntermediateAddress, value.cur_addr)
        storeTlb(IntermediateAddress, value.next_addr)
        storeTlb(Coins, value.fwd_fee_remaining)
        storeRef {
            storeTlb(Message.Any, value.msg)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgEnvelope = cellSlice {
        val curAddr = loadTlb(IntermediateAddress)
        val nextAddr = loadTlb(IntermediateAddress)
        val fwdFeeRemaining = loadTlb(Coins)
        val msg = loadRef {
            loadTlb(Message.Any)
        }
        MsgEnvelope(curAddr, nextAddr, fwdFeeRemaining, msg)
    }
}
