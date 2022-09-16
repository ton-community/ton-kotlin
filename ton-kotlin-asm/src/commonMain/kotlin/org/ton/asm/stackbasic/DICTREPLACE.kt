package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACE : AsmInstruction, TlbConstructorProvider<DICTREPLACE> by DICTREPLACETlbConstructor {
    override fun toString(): String = "DICTREPLACE"
}

private object DICTREPLACETlbConstructor : TlbConstructor<DICTREPLACE>(
    schema = "asm_dictreplace#f422 = DICTREPLACE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACE) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACE = DICTREPLACE
}