package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREPLACEREF : AsmInstruction, TlbConstructorProvider<DICTIREPLACEREF> by DICTIREPLACEREFTlbConstructor {
    override fun toString(): String = "DICTIREPLACEREF"
}

private object DICTIREPLACEREFTlbConstructor : TlbConstructor<DICTIREPLACEREF>(
    schema = "asm_dictireplaceref#f425 = DICTIREPLACEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREPLACEREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREPLACEREF = DICTIREPLACEREF
}
