package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDILE4Q : Instruction, TlbConstructorProvider<LDILE4Q> by LDILE4QTlbConstructor {
    override fun toString(): String = "LDILE4Q"
}

private object LDILE4QTlbConstructor : TlbConstructor<LDILE4Q>(
    schema = "asm_ldile4q#d758 = LDILE4Q;",
    type = LDILE4Q::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDILE4Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDILE4Q = LDILE4Q
}