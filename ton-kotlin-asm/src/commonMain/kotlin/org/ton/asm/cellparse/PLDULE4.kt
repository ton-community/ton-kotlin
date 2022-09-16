package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE4 : AsmInstruction, TlbConstructorProvider<PLDULE4> by PLDULE4TlbConstructor {
    override fun toString(): String = "PLDULE4"
}

private object PLDULE4TlbConstructor : TlbConstructor<PLDULE4>(
    schema = "asm_pldule4#d755 = PLDULE4;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE4 = PLDULE4
}
