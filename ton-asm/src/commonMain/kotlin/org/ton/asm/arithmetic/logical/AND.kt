package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AND : Instruction, TlbConstructorProvider<AND> by ANDTlbConstructor {
    override fun toString(): String = "AND"
}

private object ANDTlbConstructor : TlbConstructor<AND>(
    schema = "asm_and#b0 = AND;",
    type = AND::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AND) {
    }

    override fun loadTlb(cellSlice: CellSlice): AND = AND
}