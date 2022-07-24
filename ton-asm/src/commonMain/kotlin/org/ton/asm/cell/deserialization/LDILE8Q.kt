package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDILE8Q : Instruction, TlbConstructorProvider<LDILE8Q> by LDILE8QTlbConstructor {
    override fun toString(): String = "LDILE8Q"
}

private object LDILE8QTlbConstructor : TlbConstructor<LDILE8Q>(
    schema = "asm_ldile8q#d75a = LDILE8Q;",
    type = LDILE8Q::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDILE8Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDILE8Q = LDILE8Q
}