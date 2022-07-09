@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface AugDictionary<X, Y> : Iterable<Pair<X, Y>> {
    val extra: Y

    override fun iterator(): Iterator<Pair<X, Y>> = nodes().iterator()
    fun nodes(): Sequence<Pair<X, Y>>

    companion object {
        @JvmStatic
        fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCombinator<AugDictionary<X, Y>> = AugDictionaryTlbCombinator(n, x, y)
    }
}

private class AugDictionaryTlbCombinator<X, Y>(
    n: Int,
    x: TlbCodec<X>,
    y: TlbCodec<Y>
) : TlbCombinator<AugDictionary<X, Y>>() {
    val empty = AugDictionaryEmpty.tlbCodec<X, Y>(y)
    val root = AugDictionaryRoot.tlbCodec(n, x, y)

    override val constructors: List<TlbConstructor<out AugDictionary<X, Y>>> =
        listOf(empty, root)

    override fun getConstructor(
        value: AugDictionary<X, Y>
    ): TlbConstructor<out AugDictionary<X, Y>> = when (value) {
        is AugDictionaryEmpty -> empty
        is AugDictionaryRoot -> root
    }
}