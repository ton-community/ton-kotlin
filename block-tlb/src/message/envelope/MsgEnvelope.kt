package org.ton.block.message.envelope

import kotlinx.serialization.SerialName
import org.ton.block.currency.Coins
import org.ton.block.message.Message
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider

public data class MsgEnvelope(
    @SerialName("cur_addr") val curAddr: IntermediateAddress,
    @SerialName("next_addr") val nextAddr: IntermediateAddress,
    @SerialName("fwd_fee_remaining") val fwdFeeRemaining: Coins,
    val msg: CellRef<Message<Cell>>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_envelope") {
            field("cur_addr", curAddr)
            field("next_addr", nextAddr)
            field("fwd_fee_remaining", fwdFeeRemaining)
            field("msg", msg)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCombinatorProvider<MsgEnvelope> by MsgEnvelopeTlbConstructor.asTlbCombinator()
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
        storeTlb(IntermediateAddress.Companion, value.curAddr)
        storeTlb(IntermediateAddress.Companion, value.nextAddr)
        storeTlb(Coins.Tlb, value.fwdFeeRemaining)
        storeRef(Message.Companion.Any, value.msg)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgEnvelope = cellSlice {
        val curAddr = loadTlb(IntermediateAddress.Companion)
        val nextAddr = loadTlb(IntermediateAddress.Companion)
        val fwdFeeRemaining = loadTlb(Coins.Tlb)
        val msg = loadRef(Message.Companion.Any)
        MsgEnvelope(curAddr, nextAddr, fwdFeeRemaining, msg)
    }
}
