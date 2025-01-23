package org.ton.block.transaction

import kotlinx.serialization.SerialName
import org.ton.block.Maybe
import org.ton.block.SplitMergeInfo
import org.ton.block.transaction.phases.ActionPhase
import org.ton.block.transaction.phases.ComputePhase
import org.ton.block.transaction.phases.CreditPhase
import org.ton.block.transaction.phases.StoragePhase
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class TransMergeInstall(
    @SerialName("split_info") val splitInfo: SplitMergeInfo,
    @SerialName("prepare_transaction") val prepareTransaction: CellRef<Transaction>,
    @SerialName("storage_ph") val storagePh: Maybe<StoragePhase>,
    @SerialName("credit_ph") val creditPh: Maybe<CreditPhase>,
    @SerialName("compute_ph") val computePh: ComputePhase,
    val action: Maybe<CellRef<ActionPhase>>,
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
    val maybeTrStoragePhase = Maybe.Companion.tlbCodec(StoragePhase.Companion)
    val maybeTrCreditPhase = Maybe.Companion.tlbCodec(CreditPhase.Companion)
    val maybeTrActionPhase = Maybe.Companion.tlbCodec(CellRef(ActionPhase.Companion))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransMergeInstall
    ) = cellBuilder {
        storeTlb(SplitMergeInfo.Companion, value.splitInfo)
        storeRef(Transaction, value.prepareTransaction)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(maybeTrCreditPhase, value.creditPh)
        storeTlb(ComputePhase.Companion, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransMergeInstall = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo.Companion)
        val prepareTransaction = loadRef(Transaction)
        val storagePh = loadTlb(maybeTrStoragePhase)
        val creditPh = loadTlb(maybeTrCreditPhase)
        val computePh = loadTlb(ComputePhase.Companion)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransMergeInstall(splitInfo, prepareTransaction, storagePh, creditPh, computePh, action, aborted, destroyed)
    }
}
