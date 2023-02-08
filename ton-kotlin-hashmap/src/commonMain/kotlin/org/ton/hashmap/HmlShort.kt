package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbNegatedConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadNegatedTlb
import org.ton.tlb.storeNegatedTlb

@Serializable
@SerialName("hml_short")
public data class HmlShort(
    val len: Unary,
    val s: BitString
) : HmLabel {
    public constructor(s: BitString) : this(Unary(s.size), s)

    override fun toBitString(): BitString = s

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("hml_short") {
            field("len", len)
            field("s", s)
        }
    }

    override fun toString(): String = print().toString()

    public companion object {
        public fun tlbCodec(): TlbNegatedConstructor<HmlShort> = HashMapLabelShortTlbConstructor
    }
}

private object HashMapLabelShortTlbConstructor : TlbNegatedConstructor<HmlShort>(
    schema = "hml_short\$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;",
    id = BitString(false)
) {
    override fun storeNegatedTlb(
        cellBuilder: CellBuilder,
        value: HmlShort
    ): Int {
        val n = cellBuilder.storeNegatedTlb(Unary, value.len)
        cellBuilder.storeBits(value.s)
        return n
    }

    override fun loadNegatedTlb(
        cellSlice: CellSlice
    ): Pair<Int, HmlShort> {
        val (n, len) = cellSlice.loadNegatedTlb(Unary)
        val s = cellSlice.loadBits(n)
        return n to HmlShort(len, s)
    }
}
