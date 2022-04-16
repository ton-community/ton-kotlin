package ton.hashmap.tlb

import ton.cell.CellReader
import ton.cell.CellWriter
import ton.hashmap.Unary
import ton.hashmap.UnarySuccess
import ton.hashmap.UnaryZero
import ton.tlb.TlbCombinator
import ton.tlb.TlbConstructor
import ton.tlb.TlbDecoder
import ton.tlb.TlbEncoder

object UnaryTlbCombinator : TlbCombinator<Unary>(
    constructors = listOf(UnaryZero.tlbCodec, UnarySuccess.tlbCodec)
) {
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
                UnarySuccess.tlbCodec.encode(cellWriter, value, typeParam, param, negativeParam)
            }
            is UnaryZero -> {
                cellWriter.writeBit(false)
                UnaryZero.tlbCodec.encode(cellWriter, value, typeParam, param, negativeParam)
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
            UnarySuccess.tlbCodec.decode(cellReader, typeParam, param, negativeParam)
        } else {
            UnaryZero.tlbCodec.decode(cellReader, typeParam, param, negativeParam)
        }
    }
}

val Unary.Companion.tlbCodec get() = UnaryTlbCombinator

object UnarySuccessTlbConstructor : TlbConstructor<UnarySuccess>(
    schema = "unary_succ\$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);"
) {
    override fun encode(
        cellWriter: CellWriter,
        value: UnarySuccess,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        var n = 0
        Unary.tlbCodec.encode(cellWriter, value.x) { n = it }
        negativeParam?.invoke(n + 1)
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?,
    ): UnarySuccess {
        var n = 0
        val x = Unary.tlbCodec.decode(cellReader) { n = it }
        negativeParam?.invoke(n + 1)
        return UnarySuccess(x)
    }
}

val UnarySuccess.Companion.tlbCodec get() = UnarySuccessTlbConstructor

object UnaryZeroTlbConstructor : TlbConstructor<UnaryZero>(
    schema = "unary_zero\$0 = Unary ~0;"
) {
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
        return UnaryZero
    }
}

val UnaryZero.tlbCodec get() = UnaryZeroTlbConstructor