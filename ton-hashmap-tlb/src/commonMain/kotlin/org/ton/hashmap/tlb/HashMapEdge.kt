package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapEdge
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

object HashMapEdgeTlbConstructor : TlbConstructor<HashMapEdge<Any>>(
        schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapEdge<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        var l = 0
        HashMapLabelTlbCombinator.encode(cellBuilder, value.label, typeParam, param) { l = it }
        val m = param - l
        HashMapNodeTlbCombinator.encode(cellBuilder, value.node, typeParam, m, negativeParam)
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapEdge<Any> {
        var l = 0
        val label = HashMapLabelTlbCombinator.decode(cellSlice, param = param) { l = it }
        val m = param - l
        val node = HashMapNodeTlbCombinator.decode(cellSlice, typeParam, m, negativeParam)
        return HashMapEdge(label, node)
    }
}

val HashMapEdge.Companion.tlbCodec get() = HashMapEdgeTlbConstructor