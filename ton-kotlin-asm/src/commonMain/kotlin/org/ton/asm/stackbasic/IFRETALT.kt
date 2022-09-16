package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFRETALT : AsmInstruction, TlbConstructorProvider<IFRETALT> by IFRETALTTlbConstructor {
    override fun toString(): String = "IFRETALT"
}

private object IFRETALTTlbConstructor : TlbConstructor<IFRETALT>(
    schema = "asm_ifretalt#e308 = IFRETALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFRETALT = IFRETALT
}