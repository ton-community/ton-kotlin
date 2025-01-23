package org.ton.block.message.inmsg

import kotlinx.serialization.SerialName
import org.ton.block.currency.Coins
import org.ton.block.message.envelope.MsgEnvelope
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class MsgDiscardTransit(
    @SerialName("in_msg") val inMsg: CellRef<MsgEnvelope>,
    @SerialName("transaction_id") val transactionId: ULong,
    @SerialName("fwd_fee") val fwdFee: Coins,
    @SerialName("proof_delivered") val proofDelivered: Cell
) : InMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_discard_tr") {
            field("in_msg", inMsg)
            field("transaction_id", transactionId)
            field("fwd_fee", fwdFee)
            field("proof_delivered", proofDelivered)
        }
    }

    public companion object : TlbConstructorProvider<MsgDiscardTransit> by MsgDiscardTrTlbConstructor
}

private object MsgDiscardTrTlbConstructor : TlbConstructor<MsgDiscardTransit>(
    schema = "msg_discard_tr\$111 in_msg:^MsgEnvelope transaction_id:uint64 fwd_fee:Coins proof_delivered:^Cell = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgDiscardTransit
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeUInt64(value.transactionId)
        storeTlb(Coins.Tlb, value.fwdFee)
        storeRef(value.proofDelivered)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgDiscardTransit = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val transactionId = loadULong()
        val fwdFee = loadTlb(Coins.Tlb)
        val proofDelivered = loadRef()
        MsgDiscardTransit(inMsg, transactionId, fwdFee, proofDelivered)
    }
}
