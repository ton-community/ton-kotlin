@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface TrComputePhase {
    companion object : TlbCodec<TrComputePhase> by TrComputePhaseTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<TrComputePhase> = TrComputePhaseTlbCombinator
    }
}

private object TrComputePhaseTlbCombinator : TlbCombinator<TrComputePhase>() {
    val skipped = TrPhaseComputeSkipped.tlbCodec()
    val vm = TrPhaseComputeVm.tlbCodec()

    override val constructors: List<TlbConstructor<out TrComputePhase>> = listOf(
        skipped, vm
    )

    override fun getConstructor(
        value: TrComputePhase
    ): TlbConstructor<out TrComputePhase> = when (value) {
        is TrPhaseComputeSkipped -> skipped
        is TrPhaseComputeVm -> vm
    }
}