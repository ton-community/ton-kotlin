package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTRIPLE : AsmInstruction, TlbConstructorProvider<UNTRIPLE> by UNTRIPLETlbConstructor {
    override fun toString(): String = "UNTRIPLE"
}

private object UNTRIPLETlbConstructor : TlbConstructor<UNTRIPLE>(
    schema = "asm_untriple#6f23 = UNTRIPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTRIPLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTRIPLE = UNTRIPLE
}
