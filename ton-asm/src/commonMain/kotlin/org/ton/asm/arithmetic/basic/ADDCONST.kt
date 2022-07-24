package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class ADDCONST(
    val c: Int
) : Instruction {
    override fun toString(): String = "$c ADDCONST"

    companion object : TlbConstructorProvider<ADDCONST> by ADDCONSTTlbConstructor
}

private object ADDCONSTTlbConstructor : TlbConstructor<ADDCONST>(
    schema = "asm_addconst#a6 c:int8 = ADDCONST;",
    type = ADDCONST::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ADDCONST) {
        cellBuilder.storeInt(value.c, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): ADDCONST {
        val c = cellSlice.loadTinyInt(8).toInt()
        return ADDCONST(c)
    }
}