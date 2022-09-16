package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDZEROES : AsmInstruction, TlbConstructorProvider<LDZEROES> by LDZEROESTlbConstructor {
    override fun toString(): String = "LDZEROES"
}

private object LDZEROESTlbConstructor : TlbConstructor<LDZEROES>(
    schema = "asm_ldzeroes#d760 = LDZEROES;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDZEROES) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDZEROES = LDZEROES
}
