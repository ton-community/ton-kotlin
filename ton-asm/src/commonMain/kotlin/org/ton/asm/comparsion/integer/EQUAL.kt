package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object EQUAL : Instruction, TlbConstructorProvider<EQUAL> by EQUALTlbConstructor {
    override fun toString(): String = "EQUAL"
}

private object EQUALTlbConstructor : TlbConstructor<EQUAL>(
    schema = "asm_equal#ba = EQUAL;",
    type = EQUAL::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EQUAL) {
    }

    override fun loadTlb(cellSlice: CellSlice): EQUAL = EQUAL
}