package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETGLOBVAR : AsmInstruction, TlbConstructorProvider<SETGLOBVAR> by SETGLOBVARTlbConstructor {
    override fun toString(): String = "SETGLOBVAR"
}

private object SETGLOBVARTlbConstructor : TlbConstructor<SETGLOBVAR>(
    schema = "asm_setglobvar#f860 = SETGLOBVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETGLOBVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETGLOBVAR = SETGLOBVAR
}