package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TUPLEVAR : AsmInstruction, TlbConstructorProvider<TUPLEVAR> by TUPLEVARTlbConstructor {
    override fun toString(): String = "TUPLEVAR"
}

private object TUPLEVARTlbConstructor : TlbConstructor<TUPLEVAR>(
    schema = "asm_tuplevar#6f80 = TUPLEVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TUPLEVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): TUPLEVAR = TUPLEVAR
}
