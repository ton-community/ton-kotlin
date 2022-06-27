package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
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
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TransTickTock> = TransTickTockTlbConstructor
    }
}

private object TransTickTockTlbConstructor : TlbConstructor<TransTickTock>(
    schema = "trans_tick_tock\$001 is_tock:Bool storage_ph:TrStoragePhase\n" +
            "  compute_ph:TrComputePhase action:(Maybe ^TrActionPhase)\n" +
            "  aborted:Bool destroyed:Bool = TransactionDescr;"
) {
    val trStoragePhase by lazy { TrStoragePhase.tlbCodec() }
    val trComputePhase by lazy { TrComputePhase.tlbCodec() }
    val maybeTrActionPhase by lazy { Maybe.tlbCodec(Cell.tlbCodec(TrActionPhase.tlbCodec())) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransTickTock
    ) = cellBuilder {
        storeBit(value.is_tock)
        storeTlb(trStoragePhase, value.storage_ph)
        storeTlb(trComputePhase, value.compute_ph)
        storeTlb(maybeTrActionPhase, value.action)
        storeBit(value.aborted)
        storeBit(value.destroyed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransTickTock = cellSlice {
        val isTock = loadBit()
        val storagePh = loadTlb(trStoragePhase)
        val computePh = loadTlb(trComputePhase)
        val action = loadTlb(maybeTrActionPhase)
        val aborted = loadBit()
        val destroyed = loadBit()
        TransTickTock(isTock, storagePh, computePh, action, aborted, destroyed)
    }
}
