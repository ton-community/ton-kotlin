@file:Suppress("OPT_IN_USAGE")

package org.ton.block.transaction.phases

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@Serializable
public sealed interface BouncePhase : TlbObject {
    public companion object : TlbCombinatorProvider<BouncePhase> by TrBouncePhaseTlbCombinator
}

private object TrBouncePhaseTlbCombinator : TlbCombinator<BouncePhase>(
    BouncePhase::class,
    NegativeFundsBouncePhase::class to NegativeFundsBouncePhase,
    NoFundsBouncePhase::class to NoFundsBouncePhase,
    ExecutedBouncePhase::class to ExecutedBouncePhase,
)
