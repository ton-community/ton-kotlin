package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDILE8Q : AsmInstruction, TlbConstructorProvider<PLDILE8Q> by PLDILE8QTlbConstructor {
    override fun toString(): String = "PLDILE8Q"
}

private object PLDILE8QTlbConstructor : TlbConstructor<PLDILE8Q>(
    schema = "asm_pldile8q#d75e = PLDILE8Q;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDILE8Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDILE8Q = PLDILE8Q
}
