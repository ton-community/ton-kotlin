package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFRETALT : Instruction, TlbConstructorProvider<IFRETALT> by IFRETALTTlbConstructor {
    override fun toString(): String = "IFRETALT"
}

private object IFRETALTTlbConstructor : TlbConstructor<IFRETALT>(
    schema = "asm_ifretalt#e309 = IFRETALT;",
    type = IFRETALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFRETALT = IFRETALT
}