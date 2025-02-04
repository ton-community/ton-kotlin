package org.ton.block.transaction

import org.ton.block.Maybe
import org.ton.block.SplitMergeInfo
import org.ton.block.transaction.phases.ActionPhase
import org.ton.block.transaction.phases.ComputePhase
import org.ton.block.transaction.phases.StoragePhase
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class TransSplitPrepare(
    val splitInfo: SplitMergeInfo,
    val storagePh: Maybe<StoragePhase>,
    val computePh: ComputePhase,
    val action: Maybe<CellRef<ActionPhase>>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("trans_split_prepare") {
            field("split_info", splitInfo)
            field("storage_ph", storagePh)
            field("compute_ph", computePh)
            field("action", action)
            field("aborted", aborted)
            field("destroyed", destroyed)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TransSplitPrepare> by TransSplitPrepareTlbConstructor
}

private object TransSplitPrepareTlbConstructor : TlbConstructor<TransSplitPrepare>(
    schema = "trans_split_prepare\$0100 " +
            "  split_info:SplitMergeInfo\n" +
            "  storage_ph:(Maybe TrStoragePhase)\n" +
            "  compute_ph:TrComputePhase " +
            "  action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool\n" +
            "  = TransactionDescr;"
) {
    val maybeTrStoragePhase = Maybe.Companion(StoragePhase.Companion)
    val maybeTrActionPhase = Maybe.Companion(CellRef(ActionPhase.Companion))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransSplitPrepare
    ) = cellBuilder {
        storeTlb(SplitMergeInfo.Companion, value.splitInfo)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(ComputePhase.Companion, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBoolean(value.aborted)
        storeBoolean(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransSplitPrepare = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo.Companion)
        val storagePh = loadTlb(maybeTrStoragePhase)
        val computePh = loadTlb(ComputePhase.Companion)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransSplitPrepare(splitInfo, storagePh, computePh, action, aborted, destroyed)
    }
}
