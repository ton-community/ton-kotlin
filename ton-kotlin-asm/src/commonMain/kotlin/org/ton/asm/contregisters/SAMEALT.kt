package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SAMEALT : AsmInstruction, TlbConstructorProvider<SAMEALT> by SAMEALTTlbConstructor {
    override fun toString(): String = "SAMEALT"
}

private object SAMEALTTlbConstructor : TlbConstructor<SAMEALT>(
    schema = "asm_samealt#edfa = SAMEALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SAMEALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): SAMEALT = SAMEALT
}
