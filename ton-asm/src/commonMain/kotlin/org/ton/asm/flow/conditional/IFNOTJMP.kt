package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTJMP : Instruction, TlbConstructorProvider<IFNOTJMP> by IFNOTJMPTlbConstructor {
    override fun toString(): String = "IFNOTJMP"
}

private object IFNOTJMPTlbConstructor : TlbConstructor<IFNOTJMP>(
    schema = "asm_ifnotjmp#e1 = IFNOTJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTJMP = IFNOTJMP
}