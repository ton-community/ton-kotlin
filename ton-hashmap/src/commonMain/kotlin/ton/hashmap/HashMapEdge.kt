package ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ton.cell.CellReader
import ton.cell.CellWriter
import ton.tlb.TlbCodec
import ton.tlb.TlbDecoder
import ton.tlb.TlbEncoder

@Serializable
@SerialName("hm_edge")
data class HashMapEdge<T>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) {
    override fun toString() = "hm_edge(label=$label, node=$node)"

    companion object : TlbCodec<HashMapEdge<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapEdge<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            var l = 0
            HashMapLabel.encode(cellWriter, value.label, typeParam, param) { l = it }
            val m = param - l
            HashMapNode.encode(cellWriter, value.node, typeParam, m, negativeParam)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapEdge<Any> {
            var l = 0
            val label = HashMapLabel.decode(cellReader, param = param) { l = it }
            val m = param - l
            val node = HashMapNode.decode(cellReader, typeParam, m, negativeParam)
            return HashMapEdge(label, node)
        }
    }
}