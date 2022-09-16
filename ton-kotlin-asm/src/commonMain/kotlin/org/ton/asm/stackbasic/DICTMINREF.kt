package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTMINREF : AsmInstruction, TlbConstructorProvider<DICTMINREF> by DICTMINREFTlbConstructor {
    override fun toString(): String = "DICTMINREF"
}

private object DICTMINREFTlbConstructor : TlbConstructor<DICTMINREF>(
    schema = "asm_dictminref#f483 = DICTMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTMINREF = DICTMINREF
}