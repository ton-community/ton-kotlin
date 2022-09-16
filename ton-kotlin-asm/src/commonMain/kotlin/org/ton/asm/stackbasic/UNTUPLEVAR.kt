package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTUPLEVAR : AsmInstruction, TlbConstructorProvider<UNTUPLEVAR> by UNTUPLEVARTlbConstructor {
    override fun toString(): String = "UNTUPLEVAR"
}

private object UNTUPLEVARTlbConstructor : TlbConstructor<UNTUPLEVAR>(
    schema = "asm_untuplevar#6f82 = UNTUPLEVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTUPLEVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTUPLEVAR = UNTUPLEVAR
}