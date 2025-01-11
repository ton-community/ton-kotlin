@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@Serializable
public sealed interface TrBouncePhase : TlbObject {
    public companion object : TlbCombinatorProvider<TrBouncePhase> by TrBouncePhaseTlbCombinator
}

private object TrBouncePhaseTlbCombinator : TlbCombinator<TrBouncePhase>(
    TrBouncePhase::class,
    TrPhaseBounceNegFunds::class to TrPhaseBounceNegFunds,
    TrPhaseBounceNoFunds::class to TrPhaseBounceNoFunds,
    TrPhaseBounceOk::class to TrPhaseBounceOk,
)
