package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PLDULE8 : Instruction, TlbConstructorProvider<PLDULE8> by PLDULE8TlbConstructor {
    override fun toString(): String = "PLDULE8"
}

private object PLDULE8TlbConstructor : TlbConstructor<PLDULE8>(
    schema = "asm_pldule8#d757 = PLDULE8;",
    type = PLDULE8::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PLDULE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): PLDULE8 = PLDULE8
}