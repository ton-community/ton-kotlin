package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_compute_skipped")
public data class SkippedComputePhase(
    val reason: ComputePhaseSkipReason
) : ComputePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_compute_skipped") {
            field("reason", reason)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<SkippedComputePhase> by TrPhaseComputeSkippedTlbConstructor
}

private object TrPhaseComputeSkippedTlbConstructor : TlbConstructor<SkippedComputePhase>(
    schema = "tr_phase_compute_skipped\$0 reason:ComputeSkipReason = TrComputePhase;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: SkippedComputePhase
    ) = cellBuilder {
        storeTlb(ComputePhaseSkipReason.Companion, value.reason)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): SkippedComputePhase = cellSlice {
        val reason = loadTlb(ComputePhaseSkipReason.Companion)
        SkippedComputePhase(reason)
    }
}
