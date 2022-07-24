package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFELSE : Instruction, TlbConstructorProvider<IFELSE> by IFELSETlbConstructor {
    override fun toString(): String = "IFELSE"
}

private object IFELSETlbConstructor : TlbConstructor<IFELSE>(
    schema = "asm_ifelse#e2 = IFELSE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFELSE) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFELSE = IFELSE
}