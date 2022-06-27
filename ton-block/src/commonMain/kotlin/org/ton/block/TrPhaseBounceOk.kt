package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_bounce_ok")
data class TrPhaseBounceOk(
    val msg_size: StorageUsedShort,
    val msg_fees: Coins,
    val fwd_fees: Coins
) : TrBouncePhase {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrPhaseBounceOk> = TrPhaseBounceOkTlbConstructor
    }
}

private object TrPhaseBounceOkTlbConstructor : TlbConstructor<TrPhaseBounceOk>(
    schema = "tr_phase_bounce_ok\$1 msg_size:StorageUsedShort msg_fees:Coins fwd_fees:Coins = TrBouncePhase;"
) {
    val storageUsedShort by lazy { StorageUsedShort.tlbCodec() }
    val coins by lazy { Coins.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseBounceOk
    ) = cellBuilder {
        storeTlb(storageUsedShort, value.msg_size)
        storeTlb(coins, value.msg_fees)
        storeTlb(coins, value.fwd_fees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseBounceOk = cellSlice {
        val msgSize = loadTlb(storageUsedShort)
        val msgFees = loadTlb(coins)
        val fwdFees = loadTlb(coins)
        TrPhaseBounceOk(msgSize, msgFees, fwdFees)
    }
}