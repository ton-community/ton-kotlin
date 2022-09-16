package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACEGET : AsmInstruction, TlbConstructorProvider<DICTUREPLACEGET> by DICTUREPLACEGETTlbConstructor {
    override fun toString(): String = "DICTUREPLACEGET"
}

private object DICTUREPLACEGETTlbConstructor : TlbConstructor<DICTUREPLACEGET>(
    schema = "asm_dictureplaceget#f42e = DICTUREPLACEGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACEGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACEGET = DICTUREPLACEGET
}