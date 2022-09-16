package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUMAXREF : AsmInstruction, TlbConstructorProvider<DICTUMAXREF> by DICTUMAXREFTlbConstructor {
    override fun toString(): String = "DICTUMAXREF"
}

private object DICTUMAXREFTlbConstructor : TlbConstructor<DICTUMAXREF>(
    schema = "asm_dictumaxref#f48f = DICTUMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUMAXREF = DICTUMAXREF
}