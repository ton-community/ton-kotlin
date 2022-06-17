@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface AugDictionary<X : Any, Y : Any> {
    val extra: Y

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCombinator<AugDictionary<X, Y>> = AugDictionaryTlbCombinator(n, x, y)
    }
}

private class AugDictionaryTlbCombinator<X : Any, Y : Any>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbCombinator<AugDictionary<X, Y>>() {
    val empty by lazy {
        AugDictionaryEmpty.tlbCodec<X, Y>(y)
    }
    val root by lazy {
        AugDictionaryRoot.tlbCodec(n, x, y)
    }

    override val constructors: List<TlbConstructor<out AugDictionary<X, Y>>> by lazy {
        listOf(empty, root)
    }

    override fun getConstructor(
        value: AugDictionary<X, Y>
    ): TlbConstructor<out AugDictionary<X, Y>> = when (value) {
        is AugDictionaryEmpty -> empty
        is AugDictionaryRoot -> root
    }
}