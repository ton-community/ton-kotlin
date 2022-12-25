package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

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

    override fun toString(): String = buildString {
        append("(tr_phase_storage\nstorage_fees_collected:")
        append(storage_fees_collected)
        append(" storage_fees_due:")
        append(storage_fees_due)
        append(" status_change:")
        append(status_change)
        append(")")
    }
}

private object TrStoragePhaseTlbConstructor : TlbConstructor<TrStoragePhase>(
    schema = "tr_phase_storage\$_ storage_fees_collected:Coins " +
        "storage_fees_due:(Maybe Coins) " +
        "status_change:AccStatusChange " +
        "= TrStoragePhase;"
) {
    val maybeCoins = Maybe.tlbCodec(Coins)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrStoragePhase
    ) = cellBuilder {
        storeTlb(Coins, value.storage_fees_collected)
        storeTlb(maybeCoins, value.storage_fees_due)
        storeTlb(AccStatusChange, value.status_change)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrStoragePhase = cellSlice {
        val storageFeesCollected = loadTlb(Coins)
        val storageFeesDue = loadTlb(maybeCoins)
        val statusChange = loadTlb(AccStatusChange)
        TrStoragePhase(storageFeesCollected, storageFeesDue, statusChange)
    }
}
