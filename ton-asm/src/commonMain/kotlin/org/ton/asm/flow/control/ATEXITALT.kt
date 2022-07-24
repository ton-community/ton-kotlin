package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ATEXITALT : Instruction, TlbConstructorProvider<ATEXITALT> by ATEXITALTTlbConstructor {
    override fun toString(): String = "ATEXITALT"
}

private object ATEXITALTTlbConstructor : TlbConstructor<ATEXITALT>(
    schema = "asm_atexitalt#edf4 = ATEXITALT;",
    type = ATEXITALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ATEXITALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ATEXITALT = ATEXITALT
}