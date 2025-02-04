package org.ton.block.transaction

import kotlinx.serialization.SerialName
import org.ton.block.Maybe
import org.ton.block.transaction.phases.ActionPhase
import org.ton.block.transaction.phases.ComputePhase
import org.ton.block.transaction.phases.StoragePhase
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class TransTickTock(
    @SerialName("is_tock") val isTock: Boolean,
    @SerialName("storage_ph") val storagePh: StoragePhase,
    @SerialName("compute_ph") val computePh: ComputePhase,
    val action: Maybe<CellRef<ActionPhase>>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    override fun toString(): String = print().toString()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("trans_tick_tock") {
            field("is_tock", isTock)
            field("storage_ph", storagePh)
            field("compute_ph", computePh)
            field("action", action)
            field("aborted", aborted)
            field("destroyed", destroyed)
        }
    }

    public companion object : TlbConstructorProvider<TransTickTock> by TransTickTockTlbConstructor
}

private object TransTickTockTlbConstructor : TlbConstructor<TransTickTock>(
    schema = "trans_tick_tock\$001 is_tock:Bool storage_ph:TrStoragePhase\n" +
            "  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool = TransactionDescr;"
) {
    val maybeTrActionPhase = Maybe.Companion.tlbCodec(CellRef.tlbCodec(ActionPhase.Companion))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransTickTock
    ) = cellBuilder {
        storeBoolean(value.isTock)
        storeTlb(StoragePhase.Companion, value.storagePh)
        storeTlb(ComputePhase.Companion, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBoolean(value.aborted)
        storeBoolean(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransTickTock = cellSlice {
        val isTock = loadBit()
        val storagePh = loadTlb(StoragePhase.Companion)
        val computePh = loadTlb(ComputePhase.Companion)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransTickTock(isTock, storagePh, computePh, action, aborted, destroyed)
    }
}
