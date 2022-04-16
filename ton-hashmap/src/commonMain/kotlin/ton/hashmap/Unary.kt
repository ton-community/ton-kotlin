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
sealed class Unary {
    companion object : TlbCodec<Unary> {
        override fun encode(
            cellWriter: CellWriter,
            value: Unary,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            when (value) {
                is UnarySuccess -> {
                    cellWriter.writeBit(true)
                    UnarySuccess.encode(cellWriter, value, typeParam, param, negativeParam)
                }
                is UnaryZero -> {
                    cellWriter.writeBit(false)
                    UnaryZero.encode(cellWriter, value, typeParam, param, negativeParam)
                }
            }
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ): Unary {
            return if (cellReader.readBit()) {
                UnarySuccess.decode(cellReader, typeParam, param, negativeParam)
            } else {
                UnaryZero.decode(cellReader, typeParam, param, negativeParam)
            }
        }
    }
}

fun Unary(x: Int): UnarySuccess {
    var unary = UnarySuccess(UnaryZero)
    repeat(x - 1) {
        unary = UnarySuccess(unary)
    }
    return unary
}

@Serializable
@SerialName("unary_succ")
data class UnarySuccess(
    val x: Unary
) : Unary() {
    override fun toString() = "unary_succ(x=$x)"

    companion object : TlbCodec<UnarySuccess> {
        override fun encode(
            cellWriter: CellWriter,
            value: UnarySuccess,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
        ) {
            var n = 0
            encode(cellWriter, value.x) { n = it }
            negativeParam?.invoke(n + 1)
        }

        override fun decode(
            cellReader: CellReader,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?,
        ): UnarySuccess {
            var n = 0
            val x = Unary.decode(cellReader) { n = it }
            negativeParam?.invoke(n + 1)
            return UnarySuccess(x)
        }
    }
}

@Serializable
@SerialName("unary_zero")
object UnaryZero : Unary(), TlbCodec<UnaryZero> {
    override fun encode(
        cellWriter: CellWriter,
        value: UnaryZero,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        negativeParam?.invoke(0)
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ): UnaryZero {
        negativeParam?.invoke(0)
        return this
    }

    override fun toString() = "unary_zero"
}

