package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_storage")
data class TrStoragePhase(
    val storage_fees_collected: Coins,
    val storage_fees_due: Maybe<Coins>,
    val status_change: AccStatusChange
) {
    companion object : TlbCodec<TrStoragePhase> by TrStoragePhaseTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrStoragePhase> = TrStoragePhaseTlbConstructor
    }
}

private object TrStoragePhaseTlbConstructor : TlbConstructor<TrStoragePhase>(
    schema = "tr_phase_storage\$_ storage_fees_collected:Coins " +
            "storage_fees_due:(Maybe Coins) " +
            "status_change:AccStatusChange " +
            "= TrStoragePhase;"
) {
    val coins by lazy {
        Coins.tlbCodec()
    }
    val maybeCoins by lazy {
        Maybe.tlbCodec(coins)
    }
    val accStatusChange by lazy {
        AccStatusChange.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrStoragePhase
    ) = cellBuilder {
        storeTlb(coins, value.storage_fees_collected)
        storeTlb(maybeCoins, value.storage_fees_due)
        storeTlb(accStatusChange, value.status_change)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrStoragePhase = cellSlice {
        val storageFeesCollected = loadTlb(coins)
        val storageFeesDue = loadTlb(maybeCoins)
        val statusChange = loadTlb(accStatusChange)
        TrStoragePhase(storageFeesCollected, storageFeesDue, statusChange)
    }
}