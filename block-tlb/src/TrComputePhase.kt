@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@JsonClassDiscriminator("@type")
public sealed interface TrComputePhase : TlbObject {
    public companion object : TlbCombinatorProvider<TrComputePhase> by TrComputePhaseTlbCombinator
}

private object TrComputePhaseTlbCombinator : TlbCombinator<TrComputePhase>(
    TrComputePhase::class,
    TrPhaseComputeSkipped::class to TrPhaseComputeSkipped,
    TrPhaseComputeVm::class to TrPhaseComputeVm,
)
