package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDREFVAR : Instruction, TlbConstructorProvider<PLDREFVAR> by PLDREFVARTlbConstructor {
    override fun toString(): String = "PLDREFVAR"
}

private object PLDREFVARTlbConstructor : TlbConstructor<PLDREFVAR>(
    schema = "asm_pldrefvar#d748 = PLDREFVAR;",
    type = PLDREFVAR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDREFVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDREFVAR = PLDREFVAR
}