package org.ton.asm.compareother

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SREMPTY : AsmInstruction, TlbConstructorProvider<SREMPTY> by SREMPTYTlbConstructor {
    override fun toString(): String = "SREMPTY"
}

private object SREMPTYTlbConstructor : TlbConstructor<SREMPTY>(
    schema = "asm_srempty#c702 = SREMPTY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SREMPTY) {
    }

    override fun loadTlb(cellSlice: CellSlice): SREMPTY = SREMPTY
}
