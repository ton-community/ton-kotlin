@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface FutureSplitMerge {
    companion object : TlbCombinatorProvider<FutureSplitMerge> by FutureSplitMergeTlbCombinator
}

private object FutureSplitMergeTlbCombinator : TlbCombinator<FutureSplitMerge>() {
    val none = FutureSplitMergeNone.tlbConstructor()
    val split = FutureSplitMergeSplit.tlbConstructor()
    val merge = FutureSplitMergeMerge.tlbConstructor()

    override val constructors: List<TlbConstructor<out FutureSplitMerge>> = listOf(
        none, split, merge
    )

    override fun getConstructor(
        value: FutureSplitMerge
    ): TlbConstructor<out FutureSplitMerge> = when (value) {
        is FutureSplitMergeNone -> none
        is FutureSplitMergeSplit -> split
        is FutureSplitMergeMerge -> merge
    }
}
