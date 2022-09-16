package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TRUE : AsmInstruction, TlbConstructorProvider<TRUE> by TRUETlbConstructor {
    override fun toString(): String = "TRUE"
}

private object TRUETlbConstructor : TlbConstructor<TRUE>(
    schema = "asm_true#7f = TRUE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRUE) {
    }

    override fun loadTlb(cellSlice: CellSlice): TRUE = TRUE
}