package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE4Q : Instruction, TlbConstructorProvider<PLDULE4Q> by PLDULE4QTlbConstructor {
    override fun toString(): String = "PLDULE4Q"
}

private object PLDULE4QTlbConstructor : TlbConstructor<PLDULE4Q>(
    schema = "asm_pldule4q#d75d = PLDULE4Q;",
    type = PLDULE4Q::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE4Q = PLDULE4Q
}