package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapLabel
import org.ton.hashmap.HashMapLabelLong
import org.ton.hashmap.HashMapLabelSame
import org.ton.hashmap.HashMapLabelShort
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object HashMapLabelTlbCombinator : TlbCombinator<HashMapLabel>(
    HashMapLabelShortTlbConstructor,
    HashMapLabelLongTlbConstructor,
    HashMapLabelSameTlbConstructor
) {
    override fun getConstructor(value: HashMapLabel): TlbConstructor<out HashMapLabel> = when (value) {
        is HashMapLabelLong -> HashMapLabelLongTlbConstructor
        is HashMapLabelSame -> HashMapLabelSameTlbConstructor
        is HashMapLabelShort -> HashMapLabelShortTlbConstructor
    }
}

object HashMapLabelShortTlbConstructor : TlbConstructor<HashMapLabelShort>(
    schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: HashMapLabelShort,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        var n = 0
        storeTlb(value.len, UnaryTlbCombinator, param) { n = it }
        storeBits(value.s)
        negativeParam(n)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): HashMapLabelShort = cellSlice {
        var n = 0
        val len = loadTlb(UnaryTlbCombinator, param) { n = it }
        val s = loadBitString(n)
        negativeParam(n)
        HashMapLabelShort(len, s)
    }
}

object HashMapLabelLongTlbConstructor : TlbConstructor<HashMapLabelLong>(
    schema = "hml_long\$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: HashMapLabelLong,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUIntLeq(value.n, param)
        storeBits(value.s)
        negativeParam(value.n)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): HashMapLabelLong = cellSlice {
        val n = loadUInt(param).toInt()
        val s = loadBitString(n)
        negativeParam(n)
        HashMapLabelLong(n, s)
    }
}

object HashMapLabelSameTlbConstructor : TlbConstructor<HashMapLabelSame>(
    schema = "hml_same\$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: HashMapLabelSame,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeBit(value.v)
        storeUIntLeq(value.n, param)
        negativeParam(value.n)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): HashMapLabelSame = cellSlice {
        val v = loadBit()
        val n = loadUInt(param).toInt()
        negativeParam(n)
        HashMapLabelSame(v, n)
    }
}
