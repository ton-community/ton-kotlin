package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DUP2 : AsmInstruction, TlbConstructorProvider<DUP2> by DUP2TlbConstructor {
    override fun toString(): String = "DUP2"
}

private object DUP2TlbConstructor : TlbConstructor<DUP2>(
    schema = "asm_dup2#5c = DUP2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DUP2) {
    }

    override fun loadTlb(cellSlice: CellSlice): DUP2 = DUP2
}
