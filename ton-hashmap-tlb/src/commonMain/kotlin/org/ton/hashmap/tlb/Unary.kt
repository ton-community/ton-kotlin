package org.ton.hashmap.tlb

import org.ton.cell.CellReader
import org.ton.cell.CellWriter
import org.ton.hashmap.Unary
import org.ton.hashmap.UnarySuccess
import org.ton.hashmap.UnaryZero
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

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
                UnarySuccessTlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
            }
            is UnaryZero -> {
                cellWriter.writeBit(false)
                UnaryZeroTlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
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
            UnarySuccessTlbConstructor.decode(cellReader, typeParam, param, negativeParam)
        } else {
            UnaryZeroTlbConstructor.decode(cellReader, typeParam, param, negativeParam)
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
        UnaryTlbCombinator.encode(cellWriter, value.x) { n = it }
        negativeParam?.invoke(n + 1)
    }

    override fun decode(
        cellReader: CellReader,
        typeParam: TlbDecoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?,
    ): UnarySuccess {
        var n = 0
        val x = UnaryTlbCombinator.decode(cellReader) { n = it }
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