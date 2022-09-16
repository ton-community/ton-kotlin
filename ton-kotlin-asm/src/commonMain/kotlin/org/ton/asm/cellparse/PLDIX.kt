package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDIX : AsmInstruction, TlbConstructorProvider<PLDIX> by PLDIXTlbConstructor {
    override fun toString(): String = "PLDIX"
}

private object PLDIXTlbConstructor : TlbConstructor<PLDIX>(
    schema = "asm_pldix#d702 = PLDIX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDIX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDIX = PLDIX
}
