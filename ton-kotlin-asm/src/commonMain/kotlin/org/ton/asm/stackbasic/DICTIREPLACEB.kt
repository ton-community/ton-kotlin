package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACEB : AsmInstruction, TlbConstructorProvider<DICTIREPLACEB> by DICTIREPLACEBTlbConstructor {
    override fun toString(): String = "DICTIREPLACEB"
}

private object DICTIREPLACEBTlbConstructor : TlbConstructor<DICTIREPLACEB>(
    schema = "asm_dictireplaceb#f44a = DICTIREPLACEB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACEB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACEB = DICTIREPLACEB
}