package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object OR : Instruction, TlbConstructorProvider<OR> by ORTlbConstructor {
    override fun toString(): String = "OR"
}

private object ORTlbConstructor : TlbConstructor<OR>(
    schema = "asm_or#b1 = OR;",
    type = OR::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: OR) {
    }

    override fun loadTlb(cellSlice: CellSlice): OR = OR
}