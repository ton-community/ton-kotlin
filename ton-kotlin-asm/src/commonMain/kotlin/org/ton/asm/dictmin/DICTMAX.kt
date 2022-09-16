package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTMAX : AsmInstruction, TlbConstructorProvider<DICTMAX> by DICTMAXTlbConstructor {
    override fun toString(): String = "DICTMAX"
}

private object DICTMAXTlbConstructor : TlbConstructor<DICTMAX>(
    schema = "asm_dictmax#f48a = DICTMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTMAX = DICTMAX
}
