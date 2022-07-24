package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDILE8 : Instruction, TlbConstructorProvider<PLDILE8> by PLDILE8TlbConstructor {
    override fun toString(): String = "PLDILE8"
}

private object PLDILE8TlbConstructor : TlbConstructor<PLDILE8>(
    schema = "asm_pldile8#d756 = PLDILE8;",
    type = PLDILE8::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDILE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDILE8 = PLDILE8
}