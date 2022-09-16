package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACEGETREF : AsmInstruction, TlbConstructorProvider<DICTREPLACEGETREF> by DICTREPLACEGETREFTlbConstructor {
    override fun toString(): String = "DICTREPLACEGETREF"
}

private object DICTREPLACEGETREFTlbConstructor : TlbConstructor<DICTREPLACEGETREF>(
    schema = "asm_dictreplacegetref#f42b = DICTREPLACEGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACEGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACEGETREF = DICTREPLACEGETREF
}