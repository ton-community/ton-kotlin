package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDZEROES : Instruction, TlbConstructorProvider<LDZEROES> by LDZEROESTlbConstructor {
    override fun toString(): String = "LDZEROES"
}

private object LDZEROESTlbConstructor : TlbConstructor<LDZEROES>(
    schema = "asm_ldzeroes#d760 = LDZEROES;",
    type = LDZEROES::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDZEROES) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDZEROES = LDZEROES
}