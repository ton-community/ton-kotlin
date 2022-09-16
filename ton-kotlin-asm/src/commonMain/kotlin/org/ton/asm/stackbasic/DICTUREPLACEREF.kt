package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACEREF : AsmInstruction, TlbConstructorProvider<DICTUREPLACEREF> by DICTUREPLACEREFTlbConstructor {
    override fun toString(): String = "DICTUREPLACEREF"
}

private object DICTUREPLACEREFTlbConstructor : TlbConstructor<DICTUREPLACEREF>(
    schema = "asm_dictureplaceref#f427 = DICTUREPLACEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACEREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACEREF = DICTUREPLACEREF
}