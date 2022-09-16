package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTMIN : AsmInstruction, TlbConstructorProvider<DICTMIN> by DICTMINTlbConstructor {
    override fun toString(): String = "DICTMIN"
}

private object DICTMINTlbConstructor : TlbConstructor<DICTMIN>(
    schema = "asm_dictmin#f482 = DICTMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTMIN = DICTMIN
}
