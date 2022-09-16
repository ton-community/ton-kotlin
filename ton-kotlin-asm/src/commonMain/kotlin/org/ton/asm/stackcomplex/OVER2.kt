package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object OVER2 : AsmInstruction, TlbConstructorProvider<OVER2> by OVER2TlbConstructor {
    override fun toString(): String = "OVER2"
}

private object OVER2TlbConstructor : TlbConstructor<OVER2>(
    schema = "asm_over2#5d = OVER2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: OVER2) {
    }

    override fun loadTlb(cellSlice: CellSlice): OVER2 = OVER2
}
