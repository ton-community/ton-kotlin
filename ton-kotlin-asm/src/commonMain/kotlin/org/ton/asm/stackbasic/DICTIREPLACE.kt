package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACE : AsmInstruction, TlbConstructorProvider<DICTIREPLACE> by DICTIREPLACETlbConstructor {
    override fun toString(): String = "DICTIREPLACE"
}

private object DICTIREPLACETlbConstructor : TlbConstructor<DICTIREPLACE>(
    schema = "asm_dictireplace#f424 = DICTIREPLACE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACE) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACE = DICTIREPLACE
}