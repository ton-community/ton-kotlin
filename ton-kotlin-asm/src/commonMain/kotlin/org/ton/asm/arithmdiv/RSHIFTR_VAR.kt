package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RSHIFTR_VAR : AsmInstruction, TlbConstructorProvider<RSHIFTR_VAR> by RSHIFTR_VARTlbConstructor {
    override fun toString(): String = "RSHIFTR_VAR"
}

private object RSHIFTR_VARTlbConstructor : TlbConstructor<RSHIFTR_VAR>(
    schema = "asm_rshiftr_var#a925 = RSHIFTR_VAR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFTR_VAR) {
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFTR_VAR = RSHIFTR_VAR
}
