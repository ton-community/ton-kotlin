@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface FutureSplitMerge {
    companion object : TlbCombinatorProvider<FutureSplitMerge> by FutureSplitMergeTlbCombinator
}

private object FutureSplitMergeTlbCombinator : TlbCombinator<FutureSplitMerge>(
    FutureSplitMerge::class,
    FutureSplitMergeNone::class to FutureSplitMergeNone,
    FutureSplitMergeSplit::class to FutureSplitMergeSplit,
    FutureSplitMergeMerge::class to FutureSplitMergeMerge,
)
