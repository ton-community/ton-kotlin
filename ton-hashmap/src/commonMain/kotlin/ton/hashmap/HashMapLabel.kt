@file:OptIn(ExperimentalSerializationApi::class)

package ton.hashmap

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.bitstring.BitString
import ton.cell.CellReader
import ton.cell.CellWriter
import ton.tlb.*

@Serializable
@JsonClassDiscriminator("@type")
sealed class HashMapLabel {
    companion object : TlbCodec<HashMapLabel> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapLabel,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            when (value) {
                is HashMapLabelSame -> {
                    cellWriter.writeBits(true, true)
                    HashMapLabelSame.encode(cellWriter, value, typeParam, param, negativeParam)
                }
                is HashMapLabelLong -> {
                    cellWriter.writeBits(true, false)
                    HashMapLabelLong.encode(cellWriter, value, typeParam, param, negativeParam)
                }
                is HashMapLabelShort -> {
                    cellWriter.writeBits(false)
                    HashMapLabelShort.encode(cellWriter, value, typeParam, param, negativeParam)
                }
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapLabel {
            return if (cellReader.readBit()) {
                if (cellReader.readBit()) {
                    HashMapLabelSame.decode(cellReader, typeParam, param, negativeParam)
                } else {
                    HashMapLabelLong.decode(cellReader, typeParam, param, negativeParam)
                }
            } else {
                HashMapLabelShort.decode(cellReader, typeParam, param, negativeParam)
            }
        }
    }
}

@Serializable
@SerialName("hml_short")
data class HashMapLabelShort(
    val len: Unary,
    val s: BitString
) : HashMapLabel() {
    constructor(s: BitString) : this(Unary(s.size), s)

    override fun toString() = "hml_short(len=$len, s=$s)"

    companion object : TlbCodec<HashMapLabelShort> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapLabelShort,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            var n = 0
            Unary.encode(cellWriter, value.len, typeParam, param) { n = it }
            cellWriter.writeBitString(value.s)
            negativeParam?.invoke(n)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapLabelShort {
            var n = 0
            val len = Unary.decode(cellReader) { n = it }
            val s = cellReader.readBitString(n)
            negativeParam?.invoke(n)
            return HashMapLabelShort(len, s)
        }
    }
}

@Serializable
@SerialName("hml_long")
data class HashMapLabelLong(
    val n: Int,
    val s: BitString
) : HashMapLabel() {
    constructor(s: BitString) : this(s.size, s)

    override fun toString() = "hml_long(n=$n, s=$s)"

    companion object : TlbCodec<HashMapLabelLong> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapLabelLong,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            cellWriter.writeIntLeq(value.n, param)
            cellWriter.writeBitString(value.s)
            negativeParam?.invoke(value.n)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapLabelLong {
            val n = cellReader.readIntLeq(param)
            val s = cellReader.readBitString(n)
            negativeParam?.invoke(n)
            return HashMapLabelLong(n, s)
        }
    }
}

@Serializable
@SerialName("hml_same")
data class HashMapLabelSame(
    val v: Boolean,
    val n: Int
) : HashMapLabel() {

    override fun toString() = "hml_same(v=$v, n=$n)"

    companion object : TlbCodec<HashMapLabelSame> {
        override fun encode(
            cellWriter: CellWriter,
            value: HashMapLabelSame,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            cellWriter.writeBit(value.v)
            cellWriter.writeIntLeq(value.n, param)
            negativeParam?.invoke(value.n)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): HashMapLabelSame {
            val v = cellReader.readBit()
            val n = cellReader.readIntLeq(param)
            negativeParam?.invoke(n)
            return HashMapLabelSame(v, n)
        }
    }
}