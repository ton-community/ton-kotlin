package org.ton.hashmap.tlb

import org.ton.cell.CellReader
import org.ton.cell.CellWriter
import org.ton.hashmap.HashMapLabel
import org.ton.hashmap.HashMapLabelLong
import org.ton.hashmap.HashMapLabelSame
import org.ton.hashmap.HashMapLabelShort
import org.ton.tlb.*

object HashMapLabelTlbCombinator : TlbCombinator<HashMapLabel>(
    constructors = listOf(HashMapLabelShort.tlbCodec, HashMapLabelLong.tlbCodec, HashMapLabelSame.tlbCodec)
) {
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
                HashMapLabelSameTlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
            }
            is HashMapLabelLong -> {
                cellWriter.writeBits(true, false)
                HashMapLabelLongTlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
            }
            is HashMapLabelShort -> {
                cellWriter.writeBits(false)
                HashMapLabelShortTlbConstructor.encode(cellWriter, value, typeParam, param, negativeParam)
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
                HashMapLabelSameTlbConstructor.decode(cellReader, typeParam, param, negativeParam)
            } else {
                HashMapLabelLongTlbConstructor.decode(cellReader, typeParam, param, negativeParam)
            }
        } else {
            HashMapLabelShortTlbConstructor.decode(cellReader, typeParam, param, negativeParam)
        }
    }
}

val HashMapLabel.Companion.tlbCodec get() = HashMapLabelTlbCombinator

object HashMapLabelShortTlbConstructor : TlbConstructor<HashMapLabelShort>(
    schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;"
) {
    override fun encode(
        cellWriter: CellWriter,
        value: HashMapLabelShort,
        typeParam: TlbEncoder<Any>?,
        param: Int,
        negativeParam: ((Int) -> Unit)?
    ) {
        var n = 0
        UnaryTlbCombinator.encode(cellWriter, value.len, typeParam, param) { n = it }
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
        val len = UnaryTlbCombinator.decode(cellReader) { n = it }
        val s = cellReader.readBitString(n)
        negativeParam?.invoke(n)
        return HashMapLabelShort(len, s)
    }
}

val HashMapLabelShort.Companion.tlbCodec get() = HashMapLabelShortTlbConstructor

object HashMapLabelLongTlbConstructor : TlbConstructor<HashMapLabelLong>(
    schema = "hml_long\$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;"
) {
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

val HashMapLabelLong.Companion.tlbCodec get() = HashMapLabelLongTlbConstructor

object HashMapLabelSameTlbConstructor : TlbConstructor<HashMapLabelSame>(
    schema = "hml_same\$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;"
) {
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

val HashMapLabelSame.Companion.tlbCodec get() = HashMapLabelSameTlbConstructor