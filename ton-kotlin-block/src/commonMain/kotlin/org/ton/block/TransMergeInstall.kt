package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("trans_merge_install")
public data class TransMergeInstall(
    @SerialName("split_info") val splitInfo: SplitMergeInfo,
    @SerialName("prepare_transaction") val prepareTransaction: CellRef<Transaction>,
    @SerialName("storage_ph") val storagePh: Maybe<TrStoragePhase>,
    @SerialName("credit_ph") val creditPh: Maybe<TrCreditPhase>,
    @SerialName("compute_ph") val computePh: TrComputePhase,
    val action: Maybe<CellRef<TrActionPhase>>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    public companion object : TlbConstructorProvider<TransMergeInstall> by TransMergeInstallTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("trans_merge_install") {
            field("split_info", splitInfo)
            field("prepare_transaction", prepareTransaction)
            field("storage_ph", storagePh)
            field("credit_ph", creditPh)
            field("compute_ph", computePh)
            field("action", action)
            field("aborted", aborted)
            field("destroyed", destroyed)
        }
    }

    override fun toString(): String = print().toString()
}

private object TransMergeInstallTlbConstructor : TlbConstructor<TransMergeInstall>(
    schema = "trans_merge_install\$0111 split_info:SplitMergeInfo\n" +
            "  prepare_transaction:^Transaction\n" +
            "  storage_ph:(Maybe TrStoragePhase)\n" +
            "  credit_ph:(Maybe TrCreditPhase)\n" +
            "  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool\n" +
            "  = TransactionDescr;"
) {
    val maybeTrStoragePhase = Maybe.tlbCodec(TrStoragePhase)
    val maybeTrCreditPhase = Maybe.tlbCodec(TrCreditPhase)
    val maybeTrActionPhase = Maybe.tlbCodec(CellRef(TrActionPhase))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransMergeInstall
    ) = cellBuilder {
        storeTlb(SplitMergeInfo, value.splitInfo)
        storeRef(Transaction, value.prepareTransaction)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(maybeTrCreditPhase, value.creditPh)
        storeTlb(TrComputePhase, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransMergeInstall = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val prepareTransaction = loadRef(Transaction)
        val storagePh = loadTlb(maybeTrStoragePhase)
        val creditPh = loadTlb(maybeTrCreditPhase)
        val computePh = loadTlb(TrComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransMergeInstall(splitInfo, prepareTransaction, storagePh, creditPh, computePh, action, aborted, destroyed)
    }
}
