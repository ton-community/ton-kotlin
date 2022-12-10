@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import kotlin.jvm.JvmStatic

@Serializable
@JsonClassDiscriminator("@type")
sealed interface TrComputePhase {
    companion object : TlbCodec<TrComputePhase> by TrComputePhaseTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<TrComputePhase> = TrComputePhaseTlbCombinator
    }
}

private object TrComputePhaseTlbCombinator : TlbCombinator<TrComputePhase>(
    TrComputePhase::class,
    TrPhaseComputeSkipped::class to TrPhaseComputeSkipped,
    TrPhaseComputeVm::class to TrPhaseComputeVm,
)
