package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREMMAXREF : AsmInstruction, TlbConstructorProvider<DICTREMMAXREF> by DICTREMMAXREFTlbConstructor {
    override fun toString(): String = "DICTREMMAXREF"
}

private object DICTREMMAXREFTlbConstructor : TlbConstructor<DICTREMMAXREF>(
    schema = "asm_dictremmaxref#f49b = DICTREMMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREMMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREMMAXREF = DICTREMMAXREF
}