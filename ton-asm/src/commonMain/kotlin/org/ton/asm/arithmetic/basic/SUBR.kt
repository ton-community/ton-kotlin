package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBR : Instruction, TlbConstructorProvider<SUBR> by SUBRTlbConstructor {
    override fun toString(): String = "SUBR"
}

private object SUBRTlbConstructor : TlbConstructor<SUBR>(
    schema = "asm_subr#a2 = SUBR;",
    type = SUBR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBR) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBR = SUBR
}