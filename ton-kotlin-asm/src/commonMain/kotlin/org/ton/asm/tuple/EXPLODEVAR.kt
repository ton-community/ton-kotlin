package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object EXPLODEVAR : AsmInstruction, TlbConstructorProvider<EXPLODEVAR> by EXPLODEVARTlbConstructor {
    override fun toString(): String = "EXPLODEVAR"
}

private object EXPLODEVARTlbConstructor : TlbConstructor<EXPLODEVAR>(
    schema = "asm_explodevar#6f84 = EXPLODEVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: EXPLODEVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): EXPLODEVAR = EXPLODEVAR
}
