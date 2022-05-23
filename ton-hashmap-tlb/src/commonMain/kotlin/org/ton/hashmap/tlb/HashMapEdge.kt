package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapEdge
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

class HashMapEdgeTlbConstructor<X : Any>(
    typeCodec: TlbCodec<X>
) : TlbConstructor<HashMapEdge<X>>(
    schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;"
) {
    private val nodeCombinator = HashMapNodeTlbCombinator(typeCodec)

    override fun encode(
        cellBuilder: CellBuilder,
        value: HashMapEdge<X>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) {
        var l = 0
        cellBuilder.storeTlb(value.label, HashMapLabelTlbCombinator, param) { l = it }
        val m = param - l
        cellBuilder.storeTlb(value.node, nodeCombinator, m)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): HashMapEdge<X> {
        var l = 0
        val label = cellSlice.loadTlb(HashMapLabelTlbCombinator, param) { l = it }
        val m = param - l
        val node = cellSlice.loadTlb(nodeCombinator, m)
        return HashMapEdge(label, node)
    }
}
