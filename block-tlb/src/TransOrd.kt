package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("trans_ord")
@Serializable
public data class TransOrd(
    @SerialName("credit_first") val creditFirst: Boolean,
    @SerialName("storage_ph") val storagePh: Maybe<TrStoragePhase>,
    @SerialName("credit_ph") val creditPh: Maybe<TrCreditPhase>,
    @SerialName("compute_ph") val computePh: TrComputePhase,
    val action: Maybe<CellRef<TrActionPhase>>,
    val aborted: Boolean,
    val bounce: Maybe<TrBouncePhase>,
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
    val maybeTrStoragePhase = Maybe.tlbCodec(TrStoragePhase)
    val maybeTrCreditPhase = Maybe.tlbCodec(TrCreditPhase)
    val maybeTrActionPhase = Maybe.tlbCodec(CellRef.tlbCodec(TrActionPhase))
    val maybeTrBouncePhase = Maybe.tlbCodec(TrBouncePhase)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransOrd
    ) = cellBuilder {
        storeBit(value.creditFirst)
        storeTlb(maybeTrStoragePhase, value.storagePh)
        storeTlb(maybeTrCreditPhase, value.creditPh)
        storeTlb(TrComputePhase, value.computePh)
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
        val computePh = loadTlb(TrComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val bounce = loadTlb(maybeTrBouncePhase)
        val destroyed = loadBit()
        TransOrd(
            creditFirst, storagePh, creditPh, computePh, action, aborted, bounce, destroyed
        )
    }
}
