package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDREF : AsmInstruction, TlbConstructorProvider<PLDREF> by PLDREFTlbConstructor {
    override fun toString(): String = "PLDREF"
}

private object PLDREFTlbConstructor : TlbConstructor<PLDREF>(
    schema = "asm_pldref#d74c = PLDREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDREF = PLDREF
}