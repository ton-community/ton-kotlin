package org.ton.asm.dictprefix

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTREPLACE : AsmInstruction, TlbConstructorProvider<PFXDICTREPLACE> by PFXDICTREPLACETlbConstructor {
    override fun toString(): String = "PFXDICTREPLACE"
}

private object PFXDICTREPLACETlbConstructor : TlbConstructor<PFXDICTREPLACE>(
    schema = "asm_pfxdictreplace#f471 = PFXDICTREPLACE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTREPLACE) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTREPLACE = PFXDICTREPLACE
}
