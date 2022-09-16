package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACEGETB : AsmInstruction, TlbConstructorProvider<DICTREPLACEGETB> by DICTREPLACEGETBTlbConstructor {
    override fun toString(): String = "DICTREPLACEGETB"
}

private object DICTREPLACEGETBTlbConstructor : TlbConstructor<DICTREPLACEGETB>(
    schema = "asm_dictreplacegetb#f44d = DICTREPLACEGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACEGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACEGETB = DICTREPLACEGETB
}