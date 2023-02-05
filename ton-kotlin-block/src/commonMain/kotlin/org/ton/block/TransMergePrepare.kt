package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("trans_merge_prepare")
public data class TransMergePrepare(
    @SerialName("split_info") val splitInfo: SplitMergeInfo,
    @SerialName("storage_ph") val storagePh: TrStoragePhase,
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
        storeTlb(SplitMergeInfo, value.splitInfo)
        storeTlb(TrStoragePhase, value.storagePh)
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
