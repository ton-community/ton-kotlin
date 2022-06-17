package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_compute_skipped")
data class TrPhaseComputeSkipped(
    val reason: ComputeSkipReason
) : TrComputePhase {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrPhaseComputeSkipped> = TrPhaseComputeSkippedTlbConstructor
    }
}

private object TrPhaseComputeSkippedTlbConstructor : TlbConstructor<TrPhaseComputeSkipped>(
    schema = "tr_phase_compute_skipped\$0 reason:ComputeSkipReason = TrComputePhase;"
) {
    val computeSkipReason by lazy {
        ComputeSkipReason.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseComputeSkipped
    ) = cellBuilder {
        storeTlb(computeSkipReason, value.reason)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseComputeSkipped = cellSlice {
        val reason = loadTlb(computeSkipReason)
        TrPhaseComputeSkipped(reason)
    }
}