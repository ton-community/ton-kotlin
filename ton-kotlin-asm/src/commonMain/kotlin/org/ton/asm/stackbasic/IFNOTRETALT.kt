package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTRETALT : AsmInstruction, TlbConstructorProvider<IFNOTRETALT> by IFNOTRETALTTlbConstructor {
    override fun toString(): String = "IFNOTRETALT"
}

private object IFNOTRETALTTlbConstructor : TlbConstructor<IFNOTRETALT>(
    schema = "asm_ifnotretalt#e309 = IFNOTRETALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTRETALT = IFNOTRETALT
}