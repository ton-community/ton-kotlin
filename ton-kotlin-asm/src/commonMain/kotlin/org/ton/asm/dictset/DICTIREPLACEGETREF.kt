package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACEGETREF : AsmInstruction,
    TlbConstructorProvider<DICTIREPLACEGETREF> by DICTIREPLACEGETREFTlbConstructor {
    override fun toString(): String = "DICTIREPLACEGETREF"
}

private object DICTIREPLACEGETREFTlbConstructor : TlbConstructor<DICTIREPLACEGETREF>(
    schema = "asm_dictireplacegetref#f42d = DICTIREPLACEGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACEGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACEGETREF = DICTIREPLACEGETREF
}
