package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFREFELSE : Instruction, TlbConstructorProvider<IFREFELSE> by IFREFELSETlbConstructor {
    override fun toString(): String = "IFREFELSE"
}

private object IFREFELSETlbConstructor : TlbConstructor<IFREFELSE>(
    schema = "asm_ifrefelse#e30d = IFREFELSE;",
    type = IFREFELSE::class,
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREFELSE) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFREFELSE = IFREFELSE
}