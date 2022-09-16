package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACEB : AsmInstruction, TlbConstructorProvider<DICTUREPLACEB> by DICTUREPLACEBTlbConstructor {
    override fun toString(): String = "DICTUREPLACEB"
}

private object DICTUREPLACEBTlbConstructor : TlbConstructor<DICTUREPLACEB>(
    schema = "asm_dictureplaceb#f44b = DICTUREPLACEB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACEB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACEB = DICTUREPLACEB
}