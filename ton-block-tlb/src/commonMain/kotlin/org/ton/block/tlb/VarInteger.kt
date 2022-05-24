package org.ton.block.tlb

import org.ton.block.VarInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

fun VarInteger.Companion.tlbCodec(): TlbCodec<VarInteger> = VarIntegerTlbConstructor()

private class VarIntegerTlbConstructor : TlbConstructor<VarInteger>(
    schema = "var_int\$_ {n:#} len:(#< n) value:(int (len * 8)) = VarInteger n;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VarInteger, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUIntLeq(value.len, param)
        storeInt(value.value, value.len * 8)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VarInteger = cellSlice {
        val len = loadUIntLeq(param).toInt()
        val value = loadInt(len)
        VarInteger(len, value)
    }
}
