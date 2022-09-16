package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDUX : AsmInstruction, TlbConstructorProvider<PLDUX> by PLDUXTlbConstructor {
    override fun toString(): String = "PLDUX"
}

private object PLDUXTlbConstructor : TlbConstructor<PLDUX>(
    schema = "asm_pldux#d703 = PLDUX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDUX) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDUX = PLDUX
}