package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("trans_ord")
@Serializable
data class TransOrd(
    val credit_first: Boolean,
    val storage_ph: Maybe<TrStoragePhase>,
    val credit_ph: Maybe<TrCreditPhase>,
    val compute_ph: TrComputePhase,
    val action: Maybe<TrActionPhase>,
    val aborted: Boolean,
    val bounce: Maybe<TrBouncePhase>,
    val destroyed: Boolean
) : TransactionDescr {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TransOrd> = TransOrdTlbConstructor
    }
}

private object TransOrdTlbConstructor : TlbConstructor<TransOrd>(
    schema = "trans_ord\$0000 credit_first:Bool " +
            "storage_ph:(Maybe TrStoragePhase) " +
            "credit_ph:(Maybe TrCreditPhase) " +
            "compute_ph:TrComputePhase action:(Maybe ^TrActionPhase) " +
            "aborted:Bool bounce:(Maybe TrBouncePhase) " +
            "destroyed:Bool " +
            "= TransactionDescr;"
) {
    val maybeTrStoragePhase by lazy { Maybe.tlbCodec(TrStoragePhase.tlbCodec()) }
    val maybeTrCreditPhase by lazy { Maybe.tlbCodec(TrCreditPhase.tlbCodec()) }
    val trComputePhase by lazy { TrComputePhase.tlbCodec() }
    val maybeTrActionPhase by lazy { Maybe.tlbCodec(Cell.tlbCodec(TrActionPhase.tlbCodec())) }
    val maybeTrBouncePhase by lazy { Maybe.tlbCodec(TrBouncePhase.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransOrd
    ) = cellBuilder {
        storeBit(value.credit_first)
        storeTlb(maybeTrStoragePhase, value.storage_ph)
        storeTlb(maybeTrCreditPhase, value.credit_ph)
        storeTlb(trComputePhase, value.compute_ph)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeTlb(maybeTrBouncePhase, value.bounce)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransOrd = cellSlice {
        val creditFirst = loadBit()
        val storagePh = loadTlb(maybeTrStoragePhase)
        val creditPh = loadTlb(maybeTrCreditPhase)
        val computePh = loadTlb(trComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val bounce = loadTlb(maybeTrBouncePhase)
        val destroyed = loadBit()
        TransOrd(
            creditFirst, storagePh, creditPh, computePh, action, aborted, bounce, destroyed
        )
    }
}