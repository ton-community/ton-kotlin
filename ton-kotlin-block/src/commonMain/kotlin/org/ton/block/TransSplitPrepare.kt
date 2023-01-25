package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("trans_split_prepare")
public data class TransSplitPrepare(
    val splitInfo: SplitMergeInfo,
    val storagePh: Maybe<TrStoragePhase>,
    val computePh: TrComputePhase,
    val action: Maybe<CellRef<TrActionPhase>>,
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
    val maybeTrStoragePhase = Maybe(TrStoragePhase)
    val maybeTrActionPhase = Maybe(CellRef(TrActionPhase))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransSplitPrepare
    ) = cellBuilder {
        storeTlb(SplitMergeInfo, value.splitInfo)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(TrComputePhase, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransSplitPrepare = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val storagePh = loadTlb(maybeTrStoragePhase)
        val computePh = loadTlb(TrComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransSplitPrepare(splitInfo, storagePh, computePh, action, aborted, destroyed)
    }
}
