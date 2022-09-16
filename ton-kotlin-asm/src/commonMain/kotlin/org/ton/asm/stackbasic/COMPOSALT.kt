package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object COMPOSALT : AsmInstruction, TlbConstructorProvider<COMPOSALT> by COMPOSALTTlbConstructor {
    override fun toString(): String = "COMPOSALT"
}

private object COMPOSALTTlbConstructor : TlbConstructor<COMPOSALT>(
    schema = "asm_composalt#edf1 = COMPOSALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: COMPOSALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): COMPOSALT = COMPOSALT
}