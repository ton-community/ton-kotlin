package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDULE4 : AsmInstruction, TlbConstructorProvider<LDULE4> by LDULE4TlbConstructor {
    override fun toString(): String = "LDULE4"
}

private object LDULE4TlbConstructor : TlbConstructor<LDULE4>(
    schema = "asm_ldule4#d751 = LDULE4;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDULE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDULE4 = LDULE4
}
