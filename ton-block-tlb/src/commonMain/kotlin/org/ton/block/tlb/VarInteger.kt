package org.ton.block.tlb

import org.ton.block.VarInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun VarInteger.Companion.tlbCodec(n: Int): TlbCodec<VarInteger> = VarIntegerTlbConstructor(n)

private class VarIntegerTlbConstructor(
    val n: Int
) : TlbConstructor<VarInteger>(
    schema = "var_int\$_ {n:#} len:(#< n) value:(int (len * 8)) = VarInteger n;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VarInteger
    ) = cellBuilder {
        storeUIntLeq(value.len, n)
        storeInt(value.value, value.len * 8)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VarInteger = cellSlice {
        val len = loadUIntLeq(n).toInt()
        val value = loadInt(len)
        VarInteger(len, value)
    }
}
