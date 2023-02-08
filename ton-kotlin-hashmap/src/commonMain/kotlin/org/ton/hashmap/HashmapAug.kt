package org.ton.hashmap

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public interface HashmapAug<X, Y> : TlbObject {

    /**
     * ```tl-b
     * ahm_edge#_ {n:#} {X:Type} {Y:Type} {l:#} {m:#}
     *   label:(HmLabel ~l n) {n = (~m) + l}
     *   node:(HashmapAugNode m X Y) = HashmapAug n X Y;
     */
    public interface AhmEdge<X, Y> : HashmapAug<X, Y> {
        public val n: Int
        public val l: Int
        public val m: Int get() = n - l

        public val label: HmLabel
        public val node: HashmapAugNode<X, Y>

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahm_edge") {
                field("label", label)
                field("node", node)
            }
        }

        public companion object {
            public fun <X, Y> tlbCodec(n: Int, x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<AhmEdge<X, Y>> =
                AhmEdgeTlbConstructor(n, x, y)
        }
    }

    public companion object {
        @JvmStatic
        public fun <X, Y> edge(n: Int, l: Int, label: HmLabel, node: HashmapAugNode<X, Y>): AhmEdge<X, Y> =
            AhmeEdgeImpl(n, l, label, node)

        @Suppress("UNCHECKED_CAST")
        public fun <X, Y> tlbCodec(n: Int, x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<HashmapAug<X, Y>> =
            AhmEdge.tlbCodec(n, x, y) as TlbCodec<HashmapAug<X, Y>>
    }
}

private data class AhmeEdgeImpl<X, Y>(
    override val n: Int,
    override val l: Int,
    override val label: HmLabel,
    override val node: HashmapAugNode<X, Y>,
) : HashmapAug.AhmEdge<X, Y> {
    override fun toString(): String = print().toString()
}

private class AhmEdgeTlbConstructor<X,Y>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
) : TlbConstructor<HashmapAug.AhmEdge<X, Y>>(
    schema = "ahm_edge#_ {n:#} {X:Type} {Y:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapAugNode m X Y) = HashmapAug n X Y"
) {
    override fun loadTlb(cellSlice: CellSlice): HashmapAug.AhmEdge<X, Y> {
        val (l, label) = cellSlice.loadNegatedTlb(HmLabel.tlbCodec(n))
        val m = n - l
        val node = cellSlice.loadTlb(HashmapAugNode.tlbCodec(x, y, m))
        return HashmapAug.edge(n, l, label, node)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashmapAug.AhmEdge<X, Y>) {
        check(value.n == n) { "Invalid n, expected: $n, actual: ${value.n}" }
        val l = cellBuilder.storeNegatedTlb(HmLabel.tlbCodec(n), value.label)
        val m = n - l
        cellBuilder.storeTlb(HashmapAugNode.tlbCodec(x, y, m), value.node)
    }
}
