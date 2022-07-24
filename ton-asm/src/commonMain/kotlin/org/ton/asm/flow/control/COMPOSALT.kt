package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object COMPOSALT : Instruction, TlbConstructorProvider<COMPOSALT> by COMPOSALTTlbConstructor {
    override fun toString(): String = "COMPOSALT"
}

private object COMPOSALTTlbConstructor : TlbConstructor<COMPOSALT>(
    schema = "asm_composalt#edf1 = COMPOSALT;",
    type = COMPOSALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: COMPOSALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): COMPOSALT = COMPOSALT
}