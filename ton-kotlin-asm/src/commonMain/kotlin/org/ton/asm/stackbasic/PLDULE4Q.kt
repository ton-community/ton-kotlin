package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE4Q : AsmInstruction, TlbConstructorProvider<PLDULE4Q> by PLDULE4QTlbConstructor {
    override fun toString(): String = "PLDULE4Q"
}

private object PLDULE4QTlbConstructor : TlbConstructor<PLDULE4Q>(
    schema = "asm_pldule4q#d75d = PLDULE4Q;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE4Q = PLDULE4Q
}