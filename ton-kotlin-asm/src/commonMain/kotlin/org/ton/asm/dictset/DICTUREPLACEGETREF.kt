package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACEGETREF : AsmInstruction,
    TlbConstructorProvider<DICTUREPLACEGETREF> by DICTUREPLACEGETREFTlbConstructor {
    override fun toString(): String = "DICTUREPLACEGETREF"
}

private object DICTUREPLACEGETREFTlbConstructor : TlbConstructor<DICTUREPLACEGETREF>(
    schema = "asm_dictureplacegetref#f42f = DICTUREPLACEGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACEGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACEGETREF = DICTUREPLACEGETREF
}
