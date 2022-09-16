package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDULE4Q : AsmInstruction, TlbConstructorProvider<LDULE4Q> by LDULE4QTlbConstructor {
    override fun toString(): String = "LDULE4Q"
}

private object LDULE4QTlbConstructor : TlbConstructor<LDULE4Q>(
    schema = "asm_ldule4q#d759 = LDULE4Q;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDULE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDULE4Q = LDULE4Q
}