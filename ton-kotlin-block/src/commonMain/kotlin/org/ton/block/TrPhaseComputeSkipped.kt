package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("tr_phase_compute_skipped")
data class TrPhaseComputeSkipped(
    val reason: ComputeSkipReason
) : TrComputePhase {
    override fun toString(): String = buildString {
        append("(tr_phase_compute_skipped\n")
        append("reason:")
        append(reason)
        append(")")
    }

    companion object : TlbCodec<TrPhaseComputeSkipped> by TrPhaseComputeSkippedTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrPhaseComputeSkipped> = TrPhaseComputeSkippedTlbConstructor
    }
}

private object TrPhaseComputeSkippedTlbConstructor : TlbConstructor<TrPhaseComputeSkipped>(
    schema = "tr_phase_compute_skipped\$0 reason:ComputeSkipReason = TrComputePhase;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseComputeSkipped
    ) = cellBuilder {
        storeTlb(ComputeSkipReason, value.reason)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseComputeSkipped = cellSlice {
        val reason = loadTlb(ComputeSkipReason)
        TrPhaseComputeSkipped(reason)
    }
}
