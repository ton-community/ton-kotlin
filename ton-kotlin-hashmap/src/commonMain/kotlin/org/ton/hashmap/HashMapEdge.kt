package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("hm_edge")
public data class HashMapEdge<out T>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) : Iterable<Pair<BitString, T>> {
    override fun toString(): String = "(hm_edge\nlabel:$label node:$node)"

    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()

    public fun nodes(): Sequence<Pair<BitString, T>> {
        return when (node) {
            is HashMapNodeLeaf -> sequenceOf(BitString.empty() to node.value)
            is HashMapNodeFork -> {
                // Note: left and right branches implicitly contain prefixes '0' and '1' respectively
                val left = node.left.nodes().map { (label, value) ->
                    (BitString(false) + label) to value
                }
                val right = node.right.nodes().map { (label, value) ->
                    (BitString(true) + label) to value
                }
                left + right
            }
        }.map { (childLabel, value) ->
            (label.s + childLabel) to value
        }
    }

    public companion object {
        @JvmStatic
        public fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapEdge<X>> =
            HashMapEdgeTlbConstructor(n, x)
    }
}


private class HashMapEdgeTlbConstructor<X>(
    val n: Int,
    val x: TlbCodec<X>
) : TlbConstructor<HashMapEdge<X>>(
    schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;",
    id = BitString.empty()
) {
    private val hashMapLabelCodec = HashMapLabel.tlbCodec(n)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HashMapEdge<X>
    ) {
        val l = cellBuilder.storeNegatedTlb(hashMapLabelCodec, value.label)
        val m = n - l
        cellBuilder.storeTlb(HashMapNode.tlbCodec(m, x), value.node)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HashMapEdge<X> {
        val (l, label) = cellSlice.loadNegatedTlb(hashMapLabelCodec)
        val m = n - l
        val node = cellSlice.loadTlb(HashMapNode.tlbCodec(m, x))
        return HashMapEdge(label, node)
    }
}
