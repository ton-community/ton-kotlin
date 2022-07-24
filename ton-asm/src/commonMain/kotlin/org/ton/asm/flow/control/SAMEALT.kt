package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SAMEALT : Instruction, TlbConstructorProvider<SAMEALT> by SAMEALTTlbConstructor {
    override fun toString(): String = "SAMEALT"
}

private object SAMEALTTlbConstructor : TlbConstructor<SAMEALT>(
    schema = "asm_samealt#edfa = SAMEALT;",
    type = SAMEALT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAMEALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SAMEALT = SAMEALT
}