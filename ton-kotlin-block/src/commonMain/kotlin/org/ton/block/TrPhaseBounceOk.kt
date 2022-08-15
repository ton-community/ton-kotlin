package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_bounce_ok")
data class TrPhaseBounceOk(
    val msg_size: StorageUsedShort,
    val msg_fees: Coins,
    val fwd_fees: Coins
) : TrBouncePhase {
    companion object : TlbConstructorProvider<TrPhaseBounceOk> by TrPhaseBounceOkTlbConstructor
}

private object TrPhaseBounceOkTlbConstructor : TlbConstructor<TrPhaseBounceOk>(
    schema = "tr_phase_bounce_ok\$1 msg_size:StorageUsedShort msg_fees:Coins fwd_fees:Coins = TrBouncePhase;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseBounceOk
    ) = cellBuilder {
        storeTlb(StorageUsedShort, value.msg_size)
        storeTlb(Coins, value.msg_fees)
        storeTlb(Coins, value.fwd_fees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseBounceOk = cellSlice {
        val msgSize = loadTlb(StorageUsedShort)
        val msgFees = loadTlb(Coins)
        val fwdFees = loadTlb(Coins)
        TrPhaseBounceOk(msgSize, msgFees, fwdFees)
    }
}
