@file:Suppress("OPT_IN_USAGE")

package org.ton.block.transaction.phases

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable

public sealed interface ComputePhase : TlbObject {
    public companion object : TlbCombinatorProvider<ComputePhase> by TrComputePhaseTlbCombinator
}

private object TrComputePhaseTlbCombinator : TlbCombinator<ComputePhase>(
    ComputePhase::class,
    SkippedComputePhase::class to SkippedComputePhase,
    ExecutedComputePhase::class to ExecutedComputePhase,
)
