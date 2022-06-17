@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface AugDictionaryNode<X : Any, Y : Any> {
    val extra: Y

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCombinator<AugDictionaryNode<X, Y>> = AugDictionaryNodeTlbCombinator(n, x, y)
    }
}

private class AugDictionaryNodeTlbCombinator<X : Any, Y : Any>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbCombinator<AugDictionaryNode<X, Y>>() {
    val leaf by lazy {
        AugDictionaryNodeLeaf.tlbCodec(x, y)
    }
    val fork by lazy {
        AugDictionaryNodeFork.tlbCodec(n, x, y)
    }
    override val constructors: List<TlbConstructor<out AugDictionaryNode<X, Y>>> by lazy {
        listOf(leaf, fork)
    }

    override fun getConstructor(value: AugDictionaryNode<X, Y>): TlbConstructor<out AugDictionaryNode<X, Y>> =
        when (value) {
            is AugDictionaryNodeFork -> fork
            is AugDictionaryNodeLeaf -> leaf
        }

    override fun loadTlb(cellSlice: CellSlice): AugDictionaryNode<X, Y> {
        return if (n == 0) {
            leaf.loadTlb(cellSlice)
        } else {
            fork.loadTlb(cellSlice)
        }
    }
}