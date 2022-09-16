package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RETALT : AsmInstruction, TlbConstructorProvider<RETALT> by RETALTTlbConstructor {
    override fun toString(): String = "RETALT"
}

private object RETALTTlbConstructor : TlbConstructor<RETALT>(
    schema = "asm_retalt#db31 = RETALT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETALT) {
    }

    override fun loadTlb(cellSlice: CellSlice): RETALT = RETALT
}
