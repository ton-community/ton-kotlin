package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACEGETB : AsmInstruction, TlbConstructorProvider<DICTIREPLACEGETB> by DICTIREPLACEGETBTlbConstructor {
    override fun toString(): String = "DICTIREPLACEGETB"
}

private object DICTIREPLACEGETBTlbConstructor : TlbConstructor<DICTIREPLACEGETB>(
    schema = "asm_dictireplacegetb#f44e = DICTIREPLACEGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACEGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACEGETB = DICTIREPLACEGETB
}
