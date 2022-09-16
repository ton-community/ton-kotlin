package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTMAXREF : AsmInstruction, TlbConstructorProvider<DICTMAXREF> by DICTMAXREFTlbConstructor {
    override fun toString(): String = "DICTMAXREF"
}

private object DICTMAXREFTlbConstructor : TlbConstructor<DICTMAXREF>(
    schema = "asm_dictmaxref#f48b = DICTMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTMAXREF = DICTMAXREF
}