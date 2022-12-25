@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.jvm.JvmStatic

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BinTree<X> : Iterable<X> {

    override fun iterator(): Iterator<X> = nodes().iterator()
    fun nodes(): Sequence<X>

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbCombinator<BinTree<X>> = BinTreeTlbCombinator(x) as TlbCombinator<BinTree<X>>
    }
}

private class BinTreeTlbCombinator(
    x: TlbCodec<*>
) : TlbCombinator<BinTree<*>>(
    BinTree::class,
    BinTreeLeaf::class to BinTreeLeaf.tlbCodec(x),
    BinTreeFork::class to BinTreeFork.tlbCodec(x)
)
