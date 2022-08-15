package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_import_tr")
data class MsgImportTr(
    val in_msg: MsgEnvelope,
    val out_msg: MsgEnvelope,
    val transit_fee: Coins
) : InMsg {
    companion object : TlbConstructorProvider<MsgImportTr> by MsgImportTrTlbConstructor
}

private object MsgImportTrTlbConstructor : TlbConstructor<MsgImportTr>(
    schema = "msg_import_tr\$101  in_msg:^MsgEnvelope out_msg:^MsgEnvelope transit_fee:Coins = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgImportTr
    ) = cellBuilder {
        storeRef { storeTlb(MsgEnvelope, value.in_msg) }
        storeRef { storeTlb(MsgEnvelope, value.out_msg) }
        storeTlb(Coins, value.transit_fee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgImportTr = cellSlice {
        val inMsg = loadRef { loadTlb(MsgEnvelope) }
        val outMsg = loadRef { loadTlb(MsgEnvelope) }
        val transitFee = loadTlb(Coins)
        MsgImportTr(inMsg, outMsg, transitFee)
    }
}

