package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("trans_tick_tock")
data class TransTickTock(
    val is_tock: Boolean,
    val storage_ph: TrStoragePhase,
    val compute_ph: TrComputePhase,
    val action: Maybe<TrActionPhase>,
    val aborted: Boolean,
    val destroyed: Boolean
) : TransactionDescr {
    companion object : TlbConstructorProvider<TransTickTock> by TransTickTockTlbConstructor
}

private object TransTickTockTlbConstructor : TlbConstructor<TransTickTock>(
    schema = "trans_tick_tock\$001 is_tock:Bool storage_ph:TrStoragePhase\n" +
            "  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool = TransactionDescr;"
) {
    val maybeTrActionPhase = Maybe.tlbCodec(Cell.tlbCodec(TrActionPhase))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransTickTock
    ) = cellBuilder {
        storeBit(value.is_tock)
        storeTlb(TrStoragePhase, value.storage_ph)
        storeTlb(TrComputePhase, value.compute_ph)
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
