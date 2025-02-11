package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb


@SerialName("tr_phase_compute_skipped")
public data class TrPhaseComputeSkipped(
    val reason: ComputeSkipReason
) : ComputePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_compute_skipped") {
            field("reason", reason)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TrPhaseComputeSkipped> by TrPhaseComputeSkippedTlbConstructor
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
