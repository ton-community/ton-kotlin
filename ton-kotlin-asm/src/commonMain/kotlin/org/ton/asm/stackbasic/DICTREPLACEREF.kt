package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREPLACEREF : AsmInstruction, TlbConstructorProvider<DICTREPLACEREF> by DICTREPLACEREFTlbConstructor {
    override fun toString(): String = "DICTREPLACEREF"
}

private object DICTREPLACEREFTlbConstructor : TlbConstructor<DICTREPLACEREF>(
    schema = "asm_dictreplaceref#f423 = DICTREPLACEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREPLACEREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREPLACEREF = DICTREPLACEREF
}