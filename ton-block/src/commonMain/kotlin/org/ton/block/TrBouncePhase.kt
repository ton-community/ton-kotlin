@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TrBouncePhase {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<TrBouncePhase> = TrBouncePhaseTlbCombinator
    }
}

private object TrBouncePhaseTlbCombinator : TlbCombinator<TrBouncePhase>() {
    val negFunds by lazy { TrPhaseBounceNegFunds.tlbCodec() }
    val noFunds by lazy { TrPhaseBounceNoFunds.tlbCodec() }
    val ok by lazy { TrPhaseBounceOk.tlbCodec() }

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