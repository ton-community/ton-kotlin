package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDDICT : AsmInstruction, TlbConstructorProvider<PLDDICT> by PLDDICTTlbConstructor {
    override fun toString(): String = "PLDDICT"
}

private object PLDDICTTlbConstructor : TlbConstructor<PLDDICT>(
    schema = "asm_plddict#f405 = PLDDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDDICT) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDDICT = PLDDICT
}