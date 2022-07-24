package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDULE4Q : Instruction, TlbConstructorProvider<LDULE4Q> by LDULE4QTlbConstructor {
    override fun toString(): String = "LDULE4Q"
}

private object LDULE4QTlbConstructor : TlbConstructor<LDULE4Q>(
    schema = "asm_ldule4q#d759 = LDULE4Q;",
    type = LDULE4Q::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDULE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDULE4Q = LDULE4Q
}