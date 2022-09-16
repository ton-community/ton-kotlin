package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DROP : AsmInstruction, TlbConstructorProvider<DROP> by DROPTlbConstructor {
    override fun toString(): String = "DROP"
}

private object DROPTlbConstructor : TlbConstructor<DROP>(
    schema = "asm_drop#30 = DROP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DROP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DROP = DROP
}
