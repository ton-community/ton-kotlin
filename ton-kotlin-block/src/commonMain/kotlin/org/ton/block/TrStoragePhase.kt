package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("tr_phase_storage")
public data class TrStoragePhase(
    @SerialName("storage_fees_collected") val storageFeesCollected: Coins,
    @SerialName("storage_fees_due") val storageFeesDue: Maybe<Coins>,
    @SerialName("status_change") val statusChange: AccStatusChange
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

    public companion object : TlbConstructorProvider<TrStoragePhase> by TrStoragePhaseTlbConstructor
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
        storeTlb(Coins, value.storageFeesCollected)
        storeTlb(maybeCoins, value.storageFeesDue)
        storeTlb(AccStatusChange, value.statusChange)
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
