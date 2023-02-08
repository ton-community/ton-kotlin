package org.ton.hashmap

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
        public val m: Int
        public val label: HmLabel
        public val node: HashmapAugNode<X, Y>

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahm_edge") {
                field("label", label)
                field("node", node)
            }
        }
    }

    public companion object {
        @JvmStatic
        public fun <X, Y> edge(n: Int, m: Int, label: HmLabel, node: HashmapAugNode<X, Y>): HashmapAug<X, Y> =
            AhmeEdgeImpl(n, m, label, node)
    }
}

private data class AhmeEdgeImpl<X, Y>(
    override val n: Int,
    override val m: Int,
    override val label: HmLabel,
    override val node: HashmapAugNode<X, Y>,
) : HashmapAug.AhmEdge<X, Y> {
    override fun toString(): String = print().toString()
}

private class AhmEdgeTlbConstructor<X,Y>(
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
    val n: Int
) : TlbConstructor<HashmapAug.AhmEdge<X, Y>>(
    schema = "ahm_edge#_ {n:#} {X:Type} {Y:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapAugNode m X Y) = HashmapAug n X Y"
) {
    override fun loadTlb(cellSlice: CellSlice): HashmapAug.AhmEdge<X, Y> {
        val (l, label) = cellSlice.loadNegatedTlb(HmLabel.tlbCodec(n))
        val m = n - l
        val node = cellSlice.loadTlb(HashmapAugNode.tlbCodec(x, y, m))
    }
}
