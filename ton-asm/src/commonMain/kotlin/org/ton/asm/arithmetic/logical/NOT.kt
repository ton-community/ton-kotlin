package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NOT : Instruction, TlbConstructorProvider<NOT> by NOTTlbConstructor {
    override fun toString(): String = "NOT"
}

private object NOTTlbConstructor : TlbConstructor<NOT>(
    schema = "asm_not#b3 = NOT;",
    type = NOT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOT = NOT
}