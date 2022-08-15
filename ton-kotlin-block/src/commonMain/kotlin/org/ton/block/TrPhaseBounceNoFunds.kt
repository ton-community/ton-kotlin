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
@SerialName("tr_phase_bounce_nofunds")
data class TrPhaseBounceNoFunds(
    val msg_size: StorageUsedShort,
    val req_fwd_fees: Coins
) : TrBouncePhase {
    companion object : TlbConstructorProvider<TrPhaseBounceNoFunds> by TrPhaseBounceNoFundsTlbConstructor
}

private object TrPhaseBounceNoFundsTlbConstructor : TlbConstructor<TrPhaseBounceNoFunds>(
    schema = "tr_phase_bounce_nofunds\$01 msg_size:StorageUsedShort req_fwd_fees:Coins = TrBouncePhase;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseBounceNoFunds
    ) = cellBuilder {
        storeTlb(StorageUsedShort, value.msg_size)
        storeTlb(Coins, value.req_fwd_fees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseBounceNoFunds = cellSlice {
        val msgSize = loadTlb(StorageUsedShort)
        val coins = loadTlb(Coins)
        TrPhaseBounceNoFunds(msgSize, coins)
    }
}
