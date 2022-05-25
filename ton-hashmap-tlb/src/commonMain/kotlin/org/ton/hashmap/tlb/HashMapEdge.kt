package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapEdge
import org.ton.hashmap.HashMapLabel
import org.ton.hashmap.HashMapNode
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun <X : Any> HashMapEdge.Companion.tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapEdge<X>> =
    HashMapEdgeTlbConstructor(n, x)

private class HashMapEdgeTlbConstructor<X : Any>(
    val n: Int,
    val x: TlbCodec<X>
) : TlbConstructor<HashMapEdge<X>>(
    schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;"
) {
    private val hashMapLabelCodec by lazy {
        HashMapLabel.tlbCodec(n)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HashMapEdge<X>
    ) {
        val l = cellBuilder.storeTlb(hashMapLabelCodec, value.label)
        val m = n - l
        cellBuilder.storeTlb(HashMapNode.tlbCodec(m, x), value.node)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HashMapEdge<X> {
        val (l, label) = cellSlice.loadTlb(hashMapLabelCodec)
        val m = n - l
        val node = cellSlice.loadTlb(HashMapNode.tlbCodec(m, x))
        return HashMapEdge(label, node)
    }
}
