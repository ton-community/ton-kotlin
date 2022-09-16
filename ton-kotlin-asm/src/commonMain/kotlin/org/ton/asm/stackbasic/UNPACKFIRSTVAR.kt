package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNPACKFIRSTVAR : AsmInstruction, TlbConstructorProvider<UNPACKFIRSTVAR> by UNPACKFIRSTVARTlbConstructor {
    override fun toString(): String = "UNPACKFIRSTVAR"
}

private object UNPACKFIRSTVARTlbConstructor : TlbConstructor<UNPACKFIRSTVAR>(
    schema = "asm_unpackfirstvar#6f83 = UNPACKFIRSTVAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNPACKFIRSTVAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNPACKFIRSTVAR = UNPACKFIRSTVAR
}