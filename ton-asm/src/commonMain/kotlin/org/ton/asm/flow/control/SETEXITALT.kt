package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETEXITALT : Instruction, TlbConstructorProvider<SETEXITALT> by SETEXITALTTlbConstructor {
    override fun toString(): String = "SETEXITALT"
}

private object SETEXITALTTlbConstructor : TlbConstructor<SETEXITALT>(
    schema = "asm_setexitalt#edf5 = SETEXITALT;",
    type = SETEXITALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETEXITALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETEXITALT = SETEXITALT
}