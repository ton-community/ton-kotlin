package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

@Serializable
@SerialName("hm_edge")
data class HashMapEdge<T>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) : Iterable<Pair<BitString, T>> {
    override fun toString(): String = "hm_edge(label:$label node:$node)"

    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()

    fun nodes(): Sequence<Pair<BitString, T>> {
        val parentLabel = label.s
        return when (node) {
            is HashMapNodeLeaf -> sequenceOf(parentLabel to node.value)
            is HashMapNodeFork -> (node.left.nodes() + node.right.nodes()).map { (label, value) ->
                (parentLabel + label) to value
            }
        }
    }

    companion object {
        @JvmStatic
        fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapEdge<X>> =
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
