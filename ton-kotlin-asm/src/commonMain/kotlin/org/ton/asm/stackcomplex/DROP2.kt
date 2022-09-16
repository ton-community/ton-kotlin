package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DROP2 : AsmInstruction, TlbConstructorProvider<DROP2> by DROP2TlbConstructor {
    override fun toString(): String = "DROP2"
}

private object DROP2TlbConstructor : TlbConstructor<DROP2>(
    schema = "asm_drop2#5b = DROP2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DROP2) {
    }

    override fun loadTlb(cellSlice: CellSlice): DROP2 = DROP2
}
