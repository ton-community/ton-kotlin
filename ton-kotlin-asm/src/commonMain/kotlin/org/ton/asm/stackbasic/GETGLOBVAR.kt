package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object GETGLOBVAR : AsmInstruction, TlbConstructorProvider<GETGLOBVAR> by GETGLOBVARTlbConstructor {
    override fun toString(): String = "GETGLOBVAR"
}

private object GETGLOBVARTlbConstructor : TlbConstructor<GETGLOBVAR>(
    schema = "asm_getglobvar#f840 = GETGLOBVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GETGLOBVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): GETGLOBVAR = GETGLOBVAR
}