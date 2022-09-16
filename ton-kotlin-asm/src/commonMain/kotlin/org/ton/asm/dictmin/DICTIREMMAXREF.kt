package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREMMAXREF : AsmInstruction, TlbConstructorProvider<DICTIREMMAXREF> by DICTIREMMAXREFTlbConstructor {
    override fun toString(): String = "DICTIREMMAXREF"
}

private object DICTIREMMAXREFTlbConstructor : TlbConstructor<DICTIREMMAXREF>(
    schema = "asm_dictiremmaxref#f49d = DICTIREMMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREMMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREMMAXREF = DICTIREMMAXREF
}
