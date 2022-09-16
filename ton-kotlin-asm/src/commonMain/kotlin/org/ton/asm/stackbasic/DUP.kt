package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DUP : AsmInstruction, TlbConstructorProvider<DUP> by DUPTlbConstructor {
    override fun toString(): String = "DUP"
}

private object DUPTlbConstructor : TlbConstructor<DUP>(
    schema = "asm_dup#20 = DUP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DUP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DUP = DUP
}
