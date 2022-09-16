package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETINDEXVAR : AsmInstruction, TlbConstructorProvider<SETINDEXVAR> by SETINDEXVARTlbConstructor {
    override fun toString(): String = "SETINDEXVAR"
}

private object SETINDEXVARTlbConstructor : TlbConstructor<SETINDEXVAR>(
    schema = "asm_setindexvar#6f85 = SETINDEXVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETINDEXVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETINDEXVAR = SETINDEXVAR
}