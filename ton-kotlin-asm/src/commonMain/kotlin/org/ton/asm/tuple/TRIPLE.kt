package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TRIPLE : AsmInstruction, TlbConstructorProvider<TRIPLE> by TRIPLETlbConstructor {
    override fun toString(): String = "TRIPLE"
}

private object TRIPLETlbConstructor : TlbConstructor<TRIPLE>(
    schema = "asm_triple#6f03 = TRIPLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TRIPLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): TRIPLE = TRIPLE
}
