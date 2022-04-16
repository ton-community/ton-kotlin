package ton.hashmap.tlb

import ton.cell.CellReader
import ton.cell.CellWriter
import ton.hashmap.HashMapEdge
import ton.hashmap.HashMapLabel
import ton.hashmap.HashMapNode
import ton.tlb.TlbConstructor
import ton.tlb.TlbDecoder
import ton.tlb.TlbEncoder

object HashMapEdgeTlbConstructor : TlbConstructor<HashMapEdge<Any>>(
    schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;"
) {
    override fun encode(
        cellWriter: CellWriter,
        value: HashMapEdge<Any>,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        var l = 0
        HashMapLabel.tlbCodec.encode(cellWriter, value.label, typeParam, param) { l = it }
        val m = param - l
        HashMapNode.tlbCodec.encode(cellWriter, value.node, typeParam, m, negativeParam)
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ): HashMapEdge<Any> {
        var l = 0
        val label = HashMapLabel.tlbCodec.decode(cellReader, param = param) { l = it }
        val m = param - l
        val node = HashMapNode.tlbCodec.decode(cellReader, typeParam, m, negativeParam)
        return HashMapEdge(label, node)
    }
}

val HashMapEdge.Companion.tlbCodec get() = HashMapEdgeTlbConstructor