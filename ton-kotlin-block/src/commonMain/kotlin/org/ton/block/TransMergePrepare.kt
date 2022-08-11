package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("trans_merge_prepare")
data class TransMergePrepare(
    val split_info: SplitMergeInfo,
    val storage_ph: TrStoragePhase,
    val aborted: Boolean
) : TransactionDescr {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TransMergePrepare> = TransMergePrepareTlbConstructor
    }
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
        storeTlb(SplitMergeInfo, value.split_info)
        storeTlb(TrStoragePhase, value.storage_ph)
        storeBit(value.aborted)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransMergePrepare = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val storagePh = loadTlb(TrStoragePhase)
        val aborted = loadBit()
        TransMergePrepare(splitInfo, storagePh, aborted)
    }
}
