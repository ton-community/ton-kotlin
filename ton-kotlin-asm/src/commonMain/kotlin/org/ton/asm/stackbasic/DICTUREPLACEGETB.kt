package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREPLACEGETB : AsmInstruction, TlbConstructorProvider<DICTUREPLACEGETB> by DICTUREPLACEGETBTlbConstructor {
    override fun toString(): String = "DICTUREPLACEGETB"
}

private object DICTUREPLACEGETBTlbConstructor : TlbConstructor<DICTUREPLACEGETB>(
    schema = "asm_dictureplacegetb#f44f = DICTUREPLACEGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREPLACEGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREPLACEGETB = DICTUREPLACEGETB
}