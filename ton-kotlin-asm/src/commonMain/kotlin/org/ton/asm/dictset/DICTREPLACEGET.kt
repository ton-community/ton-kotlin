package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACEGET : AsmInstruction, TlbConstructorProvider<DICTREPLACEGET> by DICTREPLACEGETTlbConstructor {
    override fun toString(): String = "DICTREPLACEGET"
}

private object DICTREPLACEGETTlbConstructor : TlbConstructor<DICTREPLACEGET>(
    schema = "asm_dictreplaceget#f42a = DICTREPLACEGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACEGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACEGET = DICTREPLACEGET
}
