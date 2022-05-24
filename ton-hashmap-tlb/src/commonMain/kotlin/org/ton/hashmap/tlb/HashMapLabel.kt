package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.*
import org.ton.tlb.*

fun HashMapLabel.Companion.tlbCodec(): TlbCodec<HashMapLabel> = HashMapLabelTlbCombinator()

private class HashMapLabelTlbCombinator : TlbCombinator<HashMapLabel>(
    HashMapLabelShortTlbConstructor,
    HashMapLabelLongTlbConstructor,
    HashMapLabelSameTlbConstructor
) {
    override fun getConstructor(value: HashMapLabel): TlbConstructor<out HashMapLabel> = when (value) {
        is HashMapLabelLong -> HashMapLabelLongTlbConstructor
        is HashMapLabelSame -> HashMapLabelSameTlbConstructor
        is HashMapLabelShort -> HashMapLabelShortTlbConstructor
    }

    object HashMapLabelShortTlbConstructor : TlbConstructor<HashMapLabelShort>(
        schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;"
    ) {
        private val unaryCodec = Unary.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapLabelShort,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            var n = 0
            storeTlb(value.len, unaryCodec, param) { n = it }
            storeBits(value.s)
            negativeParam(n)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): HashMapLabelShort = cellSlice {
            var n = 0
            val len = loadTlb(unaryCodec, param) { n = it }
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
            val n = loadUIntLeq(param).toInt()
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
            val n = loadUIntLeq(param).toInt()
            negativeParam(n)
            HashMapLabelSame(v, n)
        }
    }
}
