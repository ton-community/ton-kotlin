@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public inline fun HashMapLabel(key: BitString, max: Int = key.size): HashMapLabel = HashMapLabel.of(key, max)

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface HashMapLabel : TlbObject {
    public val s: BitString

    public companion object {
        @JvmStatic
        public fun of(key: BitString, max: Int = key.size): HashMapLabel {
            val len = 16 - max.toShort().countLeadingZeroBits()
            val longLength = 2 + len + key.size
            val shortLength = 1 + 2 * key.size + 1
            val sameLength = 2 + 1 + len
            val sameLabel = HashMapLabelSame.of(key)
            if (sameLabel != null) {
                val longOrShortLength = minOf(longLength, shortLength)
                if (sameLength < longOrShortLength) {
                    return sameLabel
                }
            }
            if (shortLength <= longLength) {
                return HashMapLabelShort(key)
            }
            return HashMapLabelLong(key)
        }

        @JvmStatic
        public fun tlbCodec(m: Int): TlbNegatedCodec<HashMapLabel> = HashMapLabelTlbCombinator(m)
    }
}

private class HashMapLabelTlbCombinator(
    m: Int,
) : TlbNegatedCombinator<HashMapLabel>(
    HashMapLabel::class,
    HashMapLabelLong::class to HashMapLabelLongTlbConstructor(m),
    HashMapLabelShort::class to HashMapLabelShortTlbConstructor,
    HashMapLabelSame::class to HashMapLabelSameTlbConstructor(m),
) {
    private object HashMapLabelShortTlbConstructor : TlbNegatedConstructor<HashMapLabelShort>(
        schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;",
        id = BitString(false)
    ) {
        override fun storeNegatedTlb(
            cellBuilder: CellBuilder,
            value: HashMapLabelShort
        ): Int {
            val n = cellBuilder.storeNegatedTlb(Unary, value.len)
            cellBuilder.storeBits(value.s)
            return n
        }

        override fun loadNegatedTlb(
            cellSlice: CellSlice
        ): Pair<Int, HashMapLabelShort> {
            val (n, len) = cellSlice.loadNegatedTlb(Unary)
            val s = cellSlice.loadBits(n)
            return n to HashMapLabelShort(len, s)
        }
    }

    private class HashMapLabelLongTlbConstructor(
        val m: Int
    ) : TlbNegatedConstructor<HashMapLabelLong>(
        schema = "hml_long\$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;",
        id = ID
    ) {
        override fun storeNegatedTlb(
            cellBuilder: CellBuilder,
            value: HashMapLabelLong
        ): Int {
            cellBuilder {
                storeUIntLeq(value.n, m)
                storeBits(value.s)
            }
            return value.n
        }

        override fun loadNegatedTlb(
            cellSlice: CellSlice
        ): Pair<Int, HashMapLabelLong> = cellSlice {
            val n = loadUIntLeq(m).toInt()
            val s = loadBits(n)
            n to HashMapLabelLong(n, s)
        }

        companion object {
            val ID = BitString(true, false)
        }
    }

    private class HashMapLabelSameTlbConstructor(
        val m: Int,
    ) : TlbNegatedConstructor<HashMapLabelSame>(
        schema = "hml_same\$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;",
        id = ID
    ) {
        override fun storeNegatedTlb(
            cellBuilder: CellBuilder,
            value: HashMapLabelSame
        ): Int {
            cellBuilder.storeBit(value.v)
            cellBuilder.storeUIntLeq(value.n, m)
            return value.n
        }

        override fun loadNegatedTlb(
            cellSlice: CellSlice
        ): Pair<Int, HashMapLabelSame> = cellSlice {
            val v = loadBit()
            val n = loadUIntLeq(m).toInt()
            n to HashMapLabelSame(v, n)
        }

        companion object {
            val ID = BitString(true, true)
        }
    }
}
