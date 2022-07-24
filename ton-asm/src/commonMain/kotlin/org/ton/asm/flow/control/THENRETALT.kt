package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object THENRETALT : Instruction, TlbConstructorProvider<THENRETALT> by THENRETALTTlbConstructor {
    override fun toString(): String = "THENRETALT"
}

private object THENRETALTTlbConstructor : TlbConstructor<THENRETALT>(
    schema = "asm_thenretalt#edf7 = THENRETALT;",
    type = THENRETALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: THENRETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): THENRETALT = THENRETALT
}