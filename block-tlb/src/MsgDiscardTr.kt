package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("msg_discard_tr")
public data class MsgDiscardTr(
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

    public companion object : TlbConstructorProvider<MsgDiscardTr> by MsgDiscardTrTlbConstructor
}

private object MsgDiscardTrTlbConstructor : TlbConstructor<MsgDiscardTr>(
    schema = "msg_discard_tr\$111 in_msg:^MsgEnvelope transaction_id:uint64 fwd_fee:Coins proof_delivered:^Cell = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgDiscardTr
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.inMsg)
        storeUInt64(value.transactionId)
        storeTlb(Coins, value.fwdFee)
        storeRef(value.proofDelivered)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgDiscardTr = cellSlice {
        val inMsg = loadRef(MsgEnvelope)
        val transactionId = loadULong()
        val fwdFee = loadTlb(Coins)
        val proofDelivered = loadRef()
        MsgDiscardTr(inMsg, transactionId, fwdFee, proofDelivered)
    }
}
