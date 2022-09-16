package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIMAXREF : AsmInstruction, TlbConstructorProvider<DICTIMAXREF> by DICTIMAXREFTlbConstructor {
    override fun toString(): String = "DICTIMAXREF"
}

private object DICTIMAXREFTlbConstructor : TlbConstructor<DICTIMAXREF>(
    schema = "asm_dictimaxref#f48d = DICTIMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIMAXREF = DICTIMAXREF
}