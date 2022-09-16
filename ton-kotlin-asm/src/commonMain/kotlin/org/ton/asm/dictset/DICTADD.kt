package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADD : AsmInstruction, TlbConstructorProvider<DICTADD> by DICTADDTlbConstructor {
    override fun toString(): String = "DICTADD"
}

private object DICTADDTlbConstructor : TlbConstructor<DICTADD>(
    schema = "asm_dictadd#f432 = DICTADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADD = DICTADD
}
