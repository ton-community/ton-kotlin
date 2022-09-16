package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACE : AsmInstruction, TlbConstructorProvider<DICTUREPLACE> by DICTUREPLACETlbConstructor {
    override fun toString(): String = "DICTUREPLACE"
}

private object DICTUREPLACETlbConstructor : TlbConstructor<DICTUREPLACE>(
    schema = "asm_dictureplace#f426 = DICTUREPLACE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACE) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACE = DICTUREPLACE
}