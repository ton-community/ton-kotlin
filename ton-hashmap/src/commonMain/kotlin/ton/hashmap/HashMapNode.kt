@file:Suppress("OPT_IN_USAGE")

package ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.cell.CellReader
import ton.cell.CellWriter
import ton.tlb.TlbCodec
import ton.tlb.TlbDecoder
import ton.tlb.TlbEncoder

@Serializable
@JsonClassDiscriminator("@type")
sealed class HashMapNode<T> {
    companion object : TlbCodec<HashMapNode<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapNode<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            when (value) {
                is HashMapNodeLeaf -> HashMapNodeLeaf.encode(cellWriter, value, typeParam, param, negativeParam)
                is HashMapNodeFork -> HashMapNodeFork.encode(cellWriter, value, typeParam, param, negativeParam)
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapNode<Any> {
            return if (param == 0) {
                HashMapNodeLeaf.decode(cellReader, typeParam, param, negativeParam)
            } else {
                HashMapNodeFork.decode(cellReader, typeParam, param, negativeParam)
            }
        }
    }
}

@Serializable
@SerialName("hmn_leaf")
data class HashMapNodeLeaf<T>(
    val value: T
) : HashMapNode<T>() {
    override fun toString() = "hmn_leaf(value=$value)"

    companion object : TlbCodec<HashMapNodeLeaf<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapNodeLeaf<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            typeParam?.encode(cellWriter, value.value, typeParam, param, negativeParam)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapNodeLeaf<Any> {
            val value = typeParam?.decode(cellReader, typeParam, param, negativeParam)
            return HashMapNodeLeaf(requireNotNull(value))
        }
    }
}

@Serializable
@SerialName("hmn_fork")
class HashMapNodeFork<T>(
    val left: HashMapEdge<T>,
    val right: HashMapEdge<T>
) : HashMapNode<T>() {
    override fun toString() = "hmn_fork(left=$left, right=$right)"

    companion object : TlbCodec<HashMapNodeFork<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapNodeFork<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            val n = param - 1
            cellWriter.writeCell {
                HashMapEdge.encode(this, value.left, typeParam, n, negativeParam)
            }
            cellWriter.writeCell {
                HashMapEdge.encode(this, value.right, typeParam, n, negativeParam)
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapNodeFork<Any> {
            val n = param - 1
            val left = HashMapEdge.decode(cellReader.readCell(), typeParam, n, negativeParam)
            val right = HashMapEdge.decode(cellReader.readCell(), typeParam, n, negativeParam)
            return HashMapNodeFork(left, right)
        }
    }
}