@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BinTree<X : Any> {
    companion object {
        @JvmStatic
        fun<X:Any> tlbCodec(
            x: TlbCodec<X>
        ): TlbCombinator<BinTree<X>> = BinTreeTlbCombinator(x)
    }
}

private class BinTreeTlbCombinator<X:Any>(
    val x: TlbCodec<X>
) : TlbCombinator<BinTree<X>>() {
    val leaf by lazy { BinTreeLeaf.tlbCodec(x) }
    val fork by lazy { BinTreeFork.tlbCodec(x) }

    override val constructors: List<TlbConstructor<out BinTree<X>>> by lazy {
        listOf(leaf, fork)
    }

    override fun getConstructor(
        value: BinTree<X>
    ): TlbConstructor<out BinTree<X>> = when(value) {
        is BinTreeLeaf -> leaf
        is BinTreeFork -> fork
    }
}