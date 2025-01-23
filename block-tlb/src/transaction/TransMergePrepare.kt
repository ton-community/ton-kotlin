package org.ton.block.transaction

import kotlinx.serialization.SerialName
import org.ton.block.SplitMergeInfo
import org.ton.block.transaction.phases.StoragePhase
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

public data class TransMergePrepare(
    @SerialName("split_info") val splitInfo: SplitMergeInfo,
    @SerialName("storage_ph") val storagePh: StoragePhase,
    val aborted: Boolean
) : TransactionDescr {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("trans_merge_prepare") {
            field("split_info", splitInfo)
            field("storage_ph", storagePh)
            field("aborted", aborted)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TransMergePrepare> by TransMergePrepareTlbConstructor
}

private object TransMergePrepareTlbConstructor : TlbConstructor<TransMergePrepare>(
    schema = "trans_merge_prepare\$0110 split_info:SplitMergeInfo\n" +
            "  storage_ph:TrStoragePhase aborted:Bool\n" +
            "  = TransactionDescr;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransMergePrepare
    ) = cellBuilder {
        storeTlb(SplitMergeInfo.Companion, value.splitInfo)
        storeTlb(StoragePhase.Companion, value.storagePh)
        storeBit(value.aborted)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransMergePrepare = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo.Companion)
        val storagePh = loadTlb(StoragePhase.Companion)
        val aborted = loadBit()
        TransMergePrepare(splitInfo, storagePh, aborted)
    }
}
