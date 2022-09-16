package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDULE8 : AsmInstruction, TlbConstructorProvider<LDULE8> by LDULE8TlbConstructor {
    override fun toString(): String = "LDULE8"
}

private object LDULE8TlbConstructor : TlbConstructor<LDULE8>(
    schema = "asm_ldule8#d753 = LDULE8;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDULE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDULE8 = LDULE8
}
