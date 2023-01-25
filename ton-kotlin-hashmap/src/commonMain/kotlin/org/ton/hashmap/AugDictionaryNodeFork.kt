package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@SerialName("ahmn_fork")
@Serializable
public data class AugDictionaryNodeFork<X, Y>(
    val left: CellRef<AugDictionaryEdge<X, Y>>,
    val right: CellRef<AugDictionaryEdge<X, Y>>,
    override val extra: Y,
) : AugDictionaryNode<X, Y> {
    public fun nodes(): Sequence<Pair<X, Y>> = sequence {
        yieldAll(left.value.nodes())
        yieldAll(right.value.nodes())
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("ahmn_fork") {
        field("left", left)
        field("right", right)
        field("extra", extra)
    }

    override fun toString(): String = print().toString()

    public companion object {
        @JvmStatic
        public fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCodec<AugDictionaryNodeFork<X, Y>> = AugDictionaryNodeForkTlbConstructor(n, x, y)
    }
}

private class AugDictionaryNodeForkTlbConstructor<X, Y>(
    n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>
) : TlbConstructor<AugDictionaryNodeFork<X, Y>>(
    schema = "ahmn_fork#_ {n:#} {X:Type} {Y:Type} left:^(AugDictionaryEdge n X Y) " +
            "right:^(AugDictionaryEdge n X Y) extra:Y = AugDictionaryNode (n + 1) X Y;",
    id = BitString.empty()
) {
    val edge = AugDictionaryEdge.tlbCodec(n - 1, x, y)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryNodeFork<X, Y>
    ) = cellBuilder {
        storeRef(edge, value.left)
        storeRef(edge, value.right)
        storeTlb(y, value.extra)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryNodeFork<X, Y> = cellSlice {
        val left = loadRef(edge)
        val right = loadRef(edge)
        val extra = loadTlb(y)
        AugDictionaryNodeFork(left, right, extra)
    }
}
