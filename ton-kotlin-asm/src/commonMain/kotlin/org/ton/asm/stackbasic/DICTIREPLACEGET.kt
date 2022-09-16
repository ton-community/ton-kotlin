package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACEGET : AsmInstruction, TlbConstructorProvider<DICTIREPLACEGET> by DICTIREPLACEGETTlbConstructor {
    override fun toString(): String = "DICTIREPLACEGET"
}

private object DICTIREPLACEGETTlbConstructor : TlbConstructor<DICTIREPLACEGET>(
    schema = "asm_dictireplaceget#f42c = DICTIREPLACEGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACEGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACEGET = DICTIREPLACEGET
}