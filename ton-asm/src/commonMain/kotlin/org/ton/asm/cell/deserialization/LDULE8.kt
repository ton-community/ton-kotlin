package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDULE8 : Instruction, TlbConstructorProvider<LDULE8> by LDULE8TlbConstructor {
    override fun toString(): String = "LDULE8"
}

private object LDULE8TlbConstructor : TlbConstructor<LDULE8>(
    schema = "asm_ldule8#d753 = LDULE8;",
    type = LDULE8::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDULE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDULE8 = LDULE8
}