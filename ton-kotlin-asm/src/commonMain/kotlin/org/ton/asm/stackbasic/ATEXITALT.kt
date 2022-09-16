package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ATEXITALT : AsmInstruction, TlbConstructorProvider<ATEXITALT> by ATEXITALTTlbConstructor {
    override fun toString(): String = "ATEXITALT"
}

private object ATEXITALTTlbConstructor : TlbConstructor<ATEXITALT>(
    schema = "asm_atexitalt#edf4 = ATEXITALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ATEXITALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ATEXITALT = ATEXITALT
}