package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ENDS : AsmInstruction, TlbConstructorProvider<ENDS> by ENDSTlbConstructor {
    override fun toString(): String = "ENDS"
}

private object ENDSTlbConstructor : TlbConstructor<ENDS>(
    schema = "asm_ends#d1 = ENDS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ENDS) {
    }

    override fun loadTlb(cellSlice: CellSlice): ENDS = ENDS
}
