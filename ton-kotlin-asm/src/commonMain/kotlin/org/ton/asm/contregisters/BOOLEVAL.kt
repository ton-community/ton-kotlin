package org.ton.asm.contregisters

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BOOLEVAL : AsmInstruction, TlbConstructorProvider<BOOLEVAL> by BOOLEVALTlbConstructor {
    override fun toString(): String = "BOOLEVAL"
}

private object BOOLEVALTlbConstructor : TlbConstructor<BOOLEVAL>(
    schema = "asm_booleval#edf9 = BOOLEVAL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BOOLEVAL) {
    }

    override fun loadTlb(cellSlice: CellSlice): BOOLEVAL = BOOLEVAL
}
