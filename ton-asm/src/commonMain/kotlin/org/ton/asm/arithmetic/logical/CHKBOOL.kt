package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKBOOL : Instruction, TlbConstructorProvider<CHKBOOL> by CHKBOOLTlbConstructor {
    override fun toString(): String = "CHKBOOL"
}

private object CHKBOOLTlbConstructor : TlbConstructor<CHKBOOL>(
    schema = "asm_chkbool#b400 = CHKBOOL;",
    type = CHKBOOL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKBOOL) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKBOOL = CHKBOOL
}