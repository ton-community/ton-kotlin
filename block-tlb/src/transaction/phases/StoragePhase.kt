package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import org.ton.block.Maybe
import org.ton.block.currency.Coins
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class StoragePhase(
    @SerialName("storage_fees_collected") val storageFeesCollected: Coins,
    @SerialName("storage_fees_due") val storageFeesDue: Maybe<Coins>,
    @SerialName("status_change") val statusChange: AccountStatusChange
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer {
            type("tr_phase_storage") {
                field("storage_fees_collected", storageFeesCollected)
                field("storage_fees_due", storageFeesDue)
                field("status_change", statusChange)
            }
        }
    }

    public companion object : TlbConstructorProvider<StoragePhase> by TrStoragePhaseTlbConstructor
}

private object TrStoragePhaseTlbConstructor : TlbConstructor<StoragePhase>(
    schema = "tr_phase_storage\$_ storage_fees_collected:Coins " +
            "storage_fees_due:(Maybe Coins) " +
            "status_change:AccStatusChange " +
            "= TrStoragePhase;"
) {
    val maybeCoins = Maybe.Companion.tlbCodec(Coins.Tlb)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: StoragePhase
    ) = cellBuilder {
        storeTlb(Coins.Tlb, value.storageFeesCollected)
        storeTlb(maybeCoins, value.storageFeesDue)
        storeTlb(AccountStatusChange.Companion, value.statusChange)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StoragePhase = cellSlice {
        val storageFeesCollected = loadTlb(Coins.Tlb)
        val storageFeesDue = loadTlb(maybeCoins)
        val statusChange = loadTlb(AccountStatusChange.Companion)
        StoragePhase(storageFeesCollected, storageFeesDue, statusChange)
    }
}
