package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDILE8Q : AsmInstruction, TlbConstructorProvider<LDILE8Q> by LDILE8QTlbConstructor {
    override fun toString(): String = "LDILE8Q"
}

private object LDILE8QTlbConstructor : TlbConstructor<LDILE8Q>(
    schema = "asm_ldile8q#d75a = LDILE8Q;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDILE8Q) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDILE8Q = LDILE8Q
}
