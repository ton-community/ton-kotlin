package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDILE4 : Instruction, TlbConstructorProvider<LDILE4> by LDILE4TlbConstructor {
    override fun toString(): String = "LDILE4"
}

private object LDILE4TlbConstructor : TlbConstructor<LDILE4>(
    schema = "asm_ldile4#d750 = LDILE4;",
    type = LDILE4::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDILE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDILE4 = LDILE4
}