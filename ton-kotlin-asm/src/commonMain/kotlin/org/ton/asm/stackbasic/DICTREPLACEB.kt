package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACEB : AsmInstruction, TlbConstructorProvider<DICTREPLACEB> by DICTREPLACEBTlbConstructor {
    override fun toString(): String = "DICTREPLACEB"
}

private object DICTREPLACEBTlbConstructor : TlbConstructor<DICTREPLACEB>(
    schema = "asm_dictreplaceb#f449 = DICTREPLACEB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACEB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACEB = DICTREPLACEB
}