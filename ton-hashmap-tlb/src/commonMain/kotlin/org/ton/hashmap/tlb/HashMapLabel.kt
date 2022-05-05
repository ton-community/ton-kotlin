package org.ton.hashmap.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapLabel
import org.ton.hashmap.HashMapLabelLong
import org.ton.hashmap.HashMapLabelSame
import org.ton.hashmap.HashMapLabelShort
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

object HashMapLabelTlbCombinator : TlbCombinator<HashMapLabel>(
        constructors = listOf(HashMapLabelShort.tlbCodec, HashMapLabelLong.tlbCodec, HashMapLabelSame.tlbCodec)
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapLabel,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        when (value) {
            is HashMapLabelSame -> {
                cellBuilder.storeBits(true, true)
                HashMapLabelSameTlbConstructor.encode(cellBuilder, value, typeParam, param, negativeParam)
            }
            is HashMapLabelLong -> {
                cellBuilder.storeBits(true, false)
                HashMapLabelLongTlbConstructor.encode(cellBuilder, value, typeParam, param, negativeParam)
            }
            is HashMapLabelShort -> {
                cellBuilder.storeBits(false)
                HashMapLabelShortTlbConstructor.encode(cellBuilder, value, typeParam, param, negativeParam)
            }
        }
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapLabel {
        return if (cellSlice.loadBit()) {
            if (cellSlice.loadBit()) {
                HashMapLabelSameTlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
            } else {
                HashMapLabelLongTlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
            }
        } else {
            HashMapLabelShortTlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
        }
    }
}

val HashMapLabel.Companion.tlbCodec get() = HashMapLabelTlbCombinator

object HashMapLabelShortTlbConstructor : TlbConstructor<HashMapLabelShort>(
        schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapLabelShort,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        var n = 0
        UnaryTlbCombinator.encode(cellBuilder, value.len, typeParam, param) { n = it }
        cellBuilder.storeBits(value.s)
        negativeParam?.invoke(n)
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapLabelShort {
        var n = 0
        val len = UnaryTlbCombinator.decode(cellSlice) { n = it }
        val s = BitString(*cellSlice.loadBits(n))
        negativeParam?.invoke(n)
        return HashMapLabelShort(len, s)
    }
}

val HashMapLabelShort.Companion.tlbCodec get() = HashMapLabelShortTlbConstructor

object HashMapLabelLongTlbConstructor : TlbConstructor<HashMapLabelLong>(
        schema = "hml_long\$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapLabelLong,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        cellBuilder.storeUIntLeq(value.n, param)
        cellBuilder.storeBits(value.s)
        negativeParam?.invoke(value.n)
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapLabelLong {
        val n = cellSlice.loadInt(param).toInt()
        val s = cellSlice.loadBitString(n)
        negativeParam?.invoke(n)
        return HashMapLabelLong(n, s)
    }
}

val HashMapLabelLong.Companion.tlbCodec get() = HashMapLabelLongTlbConstructor

object HashMapLabelSameTlbConstructor : TlbConstructor<HashMapLabelSame>(
        schema = "hml_same\$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapLabelSame,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        cellBuilder.storeBit(value.v)
        cellBuilder.storeUIntLeq(value.n, param)
        negativeParam?.invoke(value.n)
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapLabelSame {
        val v = cellSlice.loadBit()
        val n = cellSlice.loadInt(param).toInt()
        negativeParam?.invoke(n)
        return HashMapLabelSame(v, n)
    }
}

val HashMapLabelSame.Companion.tlbCodec get() = HashMapLabelSameTlbConstructor