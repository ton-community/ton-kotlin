package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THENRETALT : AsmInstruction, TlbConstructorProvider<THENRETALT> by THENRETALTTlbConstructor {
    override fun toString(): String = "THENRETALT"
}

private object THENRETALTTlbConstructor : TlbConstructor<THENRETALT>(
    schema = "asm_thenretalt#edf7 = THENRETALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THENRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): THENRETALT = THENRETALT
}