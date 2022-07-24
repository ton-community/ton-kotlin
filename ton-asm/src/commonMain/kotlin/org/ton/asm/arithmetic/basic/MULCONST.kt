package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MULCONST(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c MULCONST"

    companion object : TlbConstructorProvider<MULCONST> by MULCONSTTlbConstructor
}

private object MULCONSTTlbConstructor : TlbConstructor<MULCONST>(
    schema = "asm_mulconst#a7 c:int8 = MULCONST;",
    type = MULCONST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULCONST) {
        cellBuilder.storeInt(value.c, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MULCONST {
        val c = cellSlice.loadTinyInt(8).toInt()
        return MULCONST(c)
    }
}