package org.ton.block.transaction

import kotlinx.serialization.SerialName
import org.ton.block.Maybe
import org.ton.block.transaction.phases.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class TransOrd(
    @SerialName("credit_first") val creditFirst: Boolean,
    @SerialName("storage_ph") val storagePh: Maybe<StoragePhase>,
    @SerialName("credit_ph") val creditPh: Maybe<CreditPhase>,
    @SerialName("compute_ph") val computePh: ComputePhase,
    val action: Maybe<CellRef<ActionPhase>>,
    val aborted: Boolean,
    val bounce: Maybe<BouncePhase>,
    val destroyed: Boolean
) : TransactionDescr {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer {
            type("trans_ord") {
                field("credit_first", creditFirst)
                field("storage_ph", storagePh)
                field("credit_ph", creditPh)
                field("compute_ph", computePh)
                field("action", action)
                field("aborted", aborted)
                field("bounce", bounce)
                field("destroyed", destroyed)
            }
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TransOrd> by TransOrdTlbConstructor
}

private object TransOrdTlbConstructor : TlbConstructor<TransOrd>(
    schema = "trans_ord\$0000 credit_first:Bool " +
            "storage_ph:(Maybe TrStoragePhase) " +
            "credit_ph:(Maybe TrCreditPhase) " +
            "compute_ph:TrComputePhase " +
            "action:(Maybe ^TrActionPhase) " +
            "aborted:Bool " +
            "bounce:(Maybe TrBouncePhase) " +
            "destroyed:Bool " +
            "= TransactionDescr;"
) {
    val maybeTrStoragePhase = Maybe.Companion.tlbCodec(StoragePhase.Companion)
    val maybeTrCreditPhase = Maybe.Companion.tlbCodec(CreditPhase.Companion)
    val maybeTrActionPhase = Maybe.Companion.tlbCodec(CellRef.tlbCodec(ActionPhase.Companion))
    val maybeTrBouncePhase = Maybe.Companion.tlbCodec(BouncePhase.Companion)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransOrd
    ) = cellBuilder {
        storeBit(value.creditFirst)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(maybeTrCreditPhase, value.creditPh)
        storeTlb(ComputePhase.Companion, value.computePh)
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
        val computePh = loadTlb(ComputePhase.Companion)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val bounce = loadTlb(maybeTrBouncePhase)
        val destroyed = loadBit()
        TransOrd(
            creditFirst, storagePh, creditPh, computePh, action, aborted, bounce, destroyed
        )
    }
}
