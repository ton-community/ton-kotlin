package org.ton.hashmap

import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public interface HashmapAug<X, Y> : AugmentedDictionary<X, Y>, TlbObject {

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
    override fun get(key: BitString): Pair<X?, Y> {
        var edge: HashmapAug.AhmEdge<X, Y> = this
        var k = key
        while (true) {
            val label = edge.label.toBitString()
            val commonPrefix = k.commonPrefixWith(label.toBitString())
            when (val node = edge.node) {
                is HashmapAugNode.AhmnLeaf -> {
                    if (commonPrefix.size != label.size) {
                        return null to node.extra
                    }
                    return node.value to node.extra
                }
                is HashmapAugNode.AhmnFork -> {
                    edge = if (k[commonPrefix.size]) {
                        node.loadRight()
                    } else {
                        node.loadLeft()
                    } as HashmapAug.AhmEdge<X, Y>
                    k = k.slice(commonPrefix.size + 1)
                }
            }
        }
    }

    override fun iterator(): Iterator<AugmentedDictionary.Entry<X, Y>> = iterator {
        val label = label.toBitString()
        when (val node = node) {
            is HashmapAugNode.AhmnLeaf -> {
                val postfixLabel = BitString(n - label.size)
                yield(AugmentedDictionaryEntryImpl(label + postfixLabel, node))
            }
            is HashmapAugNode.AhmnFork -> {
                var prefixLabel = label + false
                node.loadLeft().forEach { entry ->
                    yield(AugmentedDictionaryEntryImpl(prefixLabel + entry.key, entry.leaf))
                }
                prefixLabel = label + true
                node.loadRight().forEach { entry ->
                    yield(AugmentedDictionaryEntryImpl(prefixLabel + entry.key, entry.leaf))
                }
            }
        }
    }

    override fun toString(): String = print().toString()
}

private class AugmentedDictionaryEntryImpl<X, Y>(
    override val key: BitString,
    override val leaf: AugmentedDictionary.Leaf<X, Y>
) : AugmentedDictionary.Entry<X, Y> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AugmentedDictionaryEntryImpl<*, *>) return false
        if (key != other.key) return false
        return leaf == other.leaf
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + leaf.hashCode()
        return result
    }
}

private class AhmEdgeTlbConstructor<X, Y>(
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
