package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("ahmn_fork")
@Serializable
data class AugDictionaryNodeFork<X : Any, Y : Any>(
    val left: AugDictionaryEdge<X, Y>,
    val right: AugDictionaryEdge<X, Y>,
    override val extra: Y,
) : AugDictionaryNode<X, Y> {
    override fun toString(): String = "ahmn_fork(left:$left right:$right extra:$extra)"

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryNodeFork<X, Y>> = AugDictionaryNodeForkTlbConstructor(n, x, y)
    }
}

private class AugDictionaryNodeForkTlbConstructor<X : Any, Y : Any>(
    n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbConstructor<AugDictionaryNodeFork<X, Y>>(
    schema = "ahmn_fork#_ {n:#} {X:Type} {Y:Type} left:^(AugDictionaryEdge n X Y) " +
            "right:^(AugDictionaryEdge n X Y) extra:Y = AugDictionaryNode (n + 1) X Y;"
) {
    val n = n - 1
    val edge by lazy {
        AugDictionaryEdge.tlbCodec(n, x, y)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryNodeFork<X, Y>
    ) = cellBuilder {
        storeRef {
            storeTlb(edge, value.left)
        }
        storeRef {
            storeTlb(edge, value.right)
        }
        storeTlb(y, value.extra)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryNodeFork<X, Y> = cellSlice {
        val left = loadRef {
            loadTlb(edge)
        }
        val right = loadRef {
            loadTlb(edge)
        }
        val extra = loadTlb(y)
        AugDictionaryNodeFork(left, right, extra)
    }
}