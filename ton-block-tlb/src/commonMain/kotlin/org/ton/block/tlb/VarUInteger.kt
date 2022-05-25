package org.ton.block.tlb

import org.ton.block.VarUInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun VarUInteger.Companion.tlbCodec(n: Int): TlbCodec<VarUInteger> = VarUIntegerTlbConstructor(n)

private class VarUIntegerTlbConstructor(
    val n: Int
) : TlbConstructor<VarUInteger>(
    schema = "var_uint\$_ {n:#} len:(#< n) value:(uint (len * 8)) = VarUInteger n;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VarUInteger
    ) = cellBuilder {
        storeUIntLeq(value.len, n)
        storeUInt(value.value, value.len * 8)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VarUInteger = cellSlice {
        val len = loadUIntLeq(n).toInt()
        val value = loadUInt(len)
        VarUInteger(len, value)
    }
}
