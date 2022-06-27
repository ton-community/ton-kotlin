package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.AnyTlbConstructor
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
    val intermediateAddress by lazy {
        IntermediateAddress.tlbCodec()
    }
    val coins by lazy {
        Coins.tlbCodec()
    }
    val messageAny by lazy {
        Message.tlbCodec(AnyTlbConstructor)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgEnvelope
    ) = cellBuilder {
        storeTlb(intermediateAddress, value.cur_addr)
        storeTlb(intermediateAddress, value.next_addr)
        storeTlb(coins, value.fwd_fee_remaining)
        storeRef {
            storeTlb(messageAny, value.msg)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgEnvelope = cellSlice {
        val curAddr = loadTlb(intermediateAddress)
        val nextAddr = loadTlb(intermediateAddress)
        val fwdFeeRemaining = loadTlb(coins)
        val msg = loadRef {
            loadTlb(messageAny)
        }
        MsgEnvelope(curAddr, nextAddr, fwdFeeRemaining, msg)
    }
}