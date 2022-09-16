package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREMMAXREF : AsmInstruction, TlbConstructorProvider<DICTUREMMAXREF> by DICTUREMMAXREFTlbConstructor {
    override fun toString(): String = "DICTUREMMAXREF"
}

private object DICTUREMMAXREFTlbConstructor : TlbConstructor<DICTUREMMAXREF>(
    schema = "asm_dicturemmaxref#f49f = DICTUREMMAXREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREMMAXREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREMMAXREF = DICTUREMMAXREF
}