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

private object TrBouncePhaseTlbCombinator : TlbCombinator<TrBouncePhase>() {
    val negFunds = TrPhaseBounceNegFunds.tlbConstructor()
    val noFunds = TrPhaseBounceNoFunds.tlbConstructor()
    val ok = TrPhaseBounceOk.tlbConstructor()

    override val constructors: List<TlbConstructor<out TrBouncePhase>> by lazy {
        listOf(negFunds, noFunds, ok)
    }

    override fun getConstructor(
        value: TrBouncePhase
    ): TlbConstructor<out TrBouncePhase> = when (value) {
        is TrPhaseBounceNegFunds -> negFunds
        is TrPhaseBounceNoFunds -> noFunds
        is TrPhaseBounceOk -> ok
    }
}
