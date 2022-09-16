package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFJMP : AsmInstruction, TlbConstructorProvider<IFJMP> by IFJMPTlbConstructor {
    override fun toString(): String = "IFJMP"
}

private object IFJMPTlbConstructor : TlbConstructor<IFJMP>(
    schema = "asm_ifjmp#e0 = IFJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFJMP = IFJMP
}