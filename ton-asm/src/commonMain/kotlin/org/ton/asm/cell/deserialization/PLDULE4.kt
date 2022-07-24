package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE4 : Instruction, TlbConstructorProvider<PLDULE4> by PLDULE4TlbConstructor {
    override fun toString(): String = "PLDULE4"
}

private object PLDULE4TlbConstructor : TlbConstructor<PLDULE4>(
    schema = "asm_pldule4#d755 = PLDULE4;",
    type = PLDULE4::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE4 = PLDULE4
}