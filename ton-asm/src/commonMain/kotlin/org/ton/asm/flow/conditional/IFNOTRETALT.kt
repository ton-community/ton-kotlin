package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTRETALT : Instruction, TlbConstructorProvider<IFNOTRETALT> by IFNOTRETALTTLbConstructor {
    override fun toString(): String = "IFNOTRETALT"
}

private object IFNOTRETALTTLbConstructor : TlbConstructor<IFNOTRETALT>(
    schema = "asm_ifnotretalt#e309 = IFNOTRETALT;",
    type = IFNOTRETALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTRETALT = IFNOTRETALT
}