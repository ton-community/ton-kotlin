@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TrBouncePhase {
    companion object : TlbCombinatorProvider<TrBouncePhase> by TrBouncePhaseTlbCombinator
}

private object TrBouncePhaseTlbCombinator : TlbCombinator<TrBouncePhase>(
    TrBouncePhase::class,
    TrPhaseBounceNegFunds::class to TrPhaseBounceNegFunds,
    TrPhaseBounceNoFunds::class to TrPhaseBounceNoFunds,
    TrPhaseBounceOk::class to TrPhaseBounceOk,
)
