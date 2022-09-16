package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTJMP : AsmInstruction, TlbConstructorProvider<IFNOTJMP> by IFNOTJMPTlbConstructor {
    override fun toString(): String = "IFNOTJMP"
}

private object IFNOTJMPTlbConstructor : TlbConstructor<IFNOTJMP>(
    schema = "asm_ifnotjmp#e1 = IFNOTJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTJMP = IFNOTJMP
}