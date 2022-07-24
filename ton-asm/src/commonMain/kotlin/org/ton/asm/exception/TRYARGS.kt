package org.ton.asm.exception

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class TRYARGS(
    val p: Int,
    val r: Int,
) : Instruction {
    override fun toString(): String = "$p $r TRYARGS"

    companion object : TlbConstructorProvider<TRYARGS> by TRYARGSTlbConstructor
}

private object TRYARGSTlbConstructor : TlbConstructor<TRYARGS>(
    schema = "asm_tryargs#f3 p:uint4 r:uint4 = TRYARGS;",
    type = TRYARGS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRYARGS) {
        cellBuilder.storeUInt(value.p, 4)
        cellBuilder.storeUInt(value.r, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): TRYARGS {
        val p = cellSlice.loadUInt(4).toInt()
        val r = cellSlice.loadUInt(4).toInt()
        return TRYARGS(p, r)
    }
}