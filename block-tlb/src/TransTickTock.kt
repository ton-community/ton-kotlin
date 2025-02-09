package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider


@SerialName("trans_tick_tock")
public data class TransTickTock(
    @SerialName("is_tock") val isTock: Boolean,
    @SerialName("storage_ph") val storagePh: TrStoragePhase,
    @SerialName("compute_ph") val computePh: TrComputePhase,
    val action: Maybe<CellRef<TrActionPhase>>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    override fun toString(): String = print().toString()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("trans_tick_tock") {
            field("is_tock", isTock)
            field("storage_ph", storagePh)
            field("compute_ph", computePh)
            field("action", action)
            field("aborted", aborted)
            field("destroyed", destroyed)
        }
    }

    public companion object : TlbConstructorProvider<TransTickTock> by TransTickTockTlbConstructor
}

private object TransTickTockTlbConstructor : TlbConstructor<TransTickTock>(
    schema = "trans_tick_tock\$001 is_tock:Bool storage_ph:TrStoragePhase\n" +
            "  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool = TransactionDescr;"
) {
    val maybeTrActionPhase = Maybe.tlbCodec(CellRef.tlbCodec(TrActionPhase))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransTickTock
    ) = cellBuilder {
        storeBit(value.isTock)
        storeTlb(TrStoragePhase, value.storagePh)
        storeTlb(TrComputePhase, value.computePh)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransTickTock = cellSlice {
        val isTock = loadBit()
        val storagePh = loadTlb(TrStoragePhase)
        val computePh = loadTlb(TrComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransTickTock(isTock, storagePh, computePh, action, aborted, destroyed)
    }
}
