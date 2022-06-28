package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*

@SerialName("ahm_edge")
@Serializable
data class AugDictionaryEdge<X, Y>(
    val label: HashMapLabel,
    val node: AugDictionaryNode<X, Y>
) {
    override fun toString(): String = "ahm_edge(label:$label node:$node)"

    fun nodes(): Sequence<Pair<X, Y>> {
        return when (node) {
            is AugDictionaryNodeFork -> node.nodes()
            is AugDictionaryNodeLeaf -> sequenceOf(node.value to node.extra)
        }
    }

    companion object {
        @JvmStatic
        fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryEdge<X, Y>> = AugDictionaryEdgeTlbConstructor(n, x, y)
    }
}

private class AugDictionaryEdgeTlbConstructor<X, Y>(
    val n: Int,
    val x: TlbCodec<X>,
    val y: TlbCodec<Y>,
) : TlbConstructor<AugDictionaryEdge<X, Y>>(
    schema = "ahm_edge#_ {n:#} {X:Type} {Y:Type} {l:#} {m:#} " +
            "label:(DictionaryLabel ~l n) {n = (~m) + l} " +
            "node:(AugDictionaryNode m X Y) = AugDictionaryEdge n X Y;"
) {
    val hashMapLabel by lazy {
        HashMapLabel.tlbCodec(n)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryEdge<X, Y>
    ) = cellBuilder {
        val l = storeNegatedTlb(hashMapLabel, value.label)
        val m = n - l
        storeTlb(AugDictionaryNode.tlbCodec(m, x, y), value.node)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryEdge<X, Y> = cellSlice {
        val (l, label) = loadNegatedTlb(hashMapLabel)
        val m = n - l
        val node = loadTlb(AugDictionaryNode.tlbCodec(m, x, y))
        AugDictionaryEdge(label, node)
    }
}