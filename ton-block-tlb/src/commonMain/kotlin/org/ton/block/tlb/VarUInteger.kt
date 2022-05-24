package org.ton.block.tlb

import org.ton.block.VarUInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun VarUInteger.Companion.tlbCodec(n: Int? = null): TlbCodec<VarUInteger> = VarUIntegerTlbConstructor(n)

private class VarUIntegerTlbConstructor(
    val n: Int? = null
) : TlbConstructor<VarUInteger>(
    schema = "var_uint\$_ {n:#} len:(#< n) value:(uint (len * 8)) = VarUInteger n;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VarUInteger, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUIntLeq(value.len, n ?: param)
        storeUInt(value.value, value.len * 8)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VarUInteger = cellSlice {
        val len = loadUIntLeq(n ?: param).toInt()
        val value = loadUInt(len)
        VarUInteger(len, value)
    }
}
