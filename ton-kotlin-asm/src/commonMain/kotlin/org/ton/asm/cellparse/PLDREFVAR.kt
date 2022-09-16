package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDREFVAR : AsmInstruction, TlbConstructorProvider<PLDREFVAR> by PLDREFVARTlbConstructor {
    override fun toString(): String = "PLDREFVAR"
}

private object PLDREFVARTlbConstructor : TlbConstructor<PLDREFVAR>(
    schema = "asm_pldrefvar#d748 = PLDREFVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDREFVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDREFVAR = PLDREFVAR
}
