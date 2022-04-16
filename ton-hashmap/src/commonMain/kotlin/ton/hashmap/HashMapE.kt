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
sealed class HashMapE<T> {
    companion object : TlbCodec<HashMapE<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            when (value) {
                is RootHashMapE -> RootHashMapE.encode(cellWriter, value, typeParam, param, negativeParam)
                is EmptyHashMapE -> EmptyHashMapE.encode(cellWriter, value, typeParam, param, negativeParam)
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapE<Any> {
            return if (cellReader.readBit()) {
                RootHashMapE.decode(cellReader, typeParam, param, negativeParam)
            } else {
                EmptyHashMapE.decode(cellReader, typeParam, param, negativeParam)
            }
        }
    }
}

@Serializable
@SerialName("hme_empty")
class EmptyHashMapE<T> : HashMapE<T>() {

    override fun toString() = "hme_empty"

    companion object : TlbCodec<EmptyHashMapE<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: EmptyHashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): EmptyHashMapE<Any> = EmptyHashMapE()
    }
}

@Serializable
@SerialName("hme_root")
data class RootHashMapE<T>(
    val root: HashMapEdge<T>
) : HashMapE<T>() {

    override fun toString() = "hme_root(root=$root)"

    companion object : TlbCodec<RootHashMapE<Any>> {
        override fun encode(
            cellWriter: CellWriter,
            value: RootHashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            cellWriter.writeCell {
                HashMapEdge.encode(this, value.root, typeParam, param, negativeParam)
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): RootHashMapE<Any> {
            val root = HashMapEdge.decode(cellReader.readCell(), typeParam, param, negativeParam)
            return RootHashMapE(root)
        }
    }
}