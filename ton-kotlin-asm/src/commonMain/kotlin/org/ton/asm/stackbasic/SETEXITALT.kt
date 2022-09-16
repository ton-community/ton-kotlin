package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETEXITALT : AsmInstruction, TlbConstructorProvider<SETEXITALT> by SETEXITALTTlbConstructor {
    override fun toString(): String = "SETEXITALT"
}

private object SETEXITALTTlbConstructor : TlbConstructor<SETEXITALT>(
    schema = "asm_setexitalt#edf5 = SETEXITALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETEXITALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETEXITALT = SETEXITALT
}