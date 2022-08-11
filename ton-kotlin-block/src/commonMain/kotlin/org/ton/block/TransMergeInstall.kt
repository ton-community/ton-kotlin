package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("trans_merge_install")
data class TransMergeInstall(
    val split_info: SplitMergeInfo,
    val prepare_transaction: Transaction,
    val storage_ph: Maybe<TrStoragePhase>,
    val credit_ph: Maybe<TrCreditPhase>,
    val compute_ph: TrComputePhase,
    val action: Maybe<TrActionPhase>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TransMergeInstall> = TransMergeInstallTlbConstructor
    }
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
    val maybeTrStoragePhase by lazy { Maybe.tlbCodec(TrStoragePhase.tlbCodec()) }
    val maybeTrCreditPhase by lazy { Maybe.tlbCodec(TrCreditPhase.tlbCodec()) }
    val trComputePhase by lazy { TrComputePhase.tlbCodec() }
    val maybeTrActionPhase by lazy { Maybe.tlbCodec(TrActionPhase.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransMergeInstall
    ) = cellBuilder {
        storeTlb(SplitMergeInfo, value.split_info)
        storeRef { storeTlb(Transaction, value.prepare_transaction) }
        storeTlb(maybeTrStoragePhase, value.storage_ph)
        storeTlb(maybeTrCreditPhase, value.credit_ph)
        storeTlb(trComputePhase, value.compute_ph)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransMergeInstall = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val prepareTransaction = loadRef { loadTlb(Transaction) }
        val storagePh = loadTlb(maybeTrStoragePhase)
        val creditPh = loadTlb(maybeTrCreditPhase)
        val computePh = loadTlb(trComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransMergeInstall(splitInfo, prepareTransaction, storagePh, creditPh, computePh, action, aborted, destroyed)
    }
}
