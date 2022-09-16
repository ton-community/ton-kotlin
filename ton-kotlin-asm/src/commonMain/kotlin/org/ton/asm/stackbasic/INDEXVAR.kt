package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object INDEXVAR : AsmInstruction, TlbConstructorProvider<INDEXVAR> by INDEXVARTlbConstructor {
    override fun toString(): String = "INDEXVAR"
}

private object INDEXVARTlbConstructor : TlbConstructor<INDEXVAR>(
    schema = "asm_indexvar#6f81 = INDEXVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: INDEXVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): INDEXVAR = INDEXVAR
}