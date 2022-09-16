package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTDELGET : AsmInstruction, TlbConstructorProvider<DICTDELGET> by DICTDELGETTlbConstructor {
    override fun toString(): String = "DICTDELGET"
}

private object DICTDELGETTlbConstructor : TlbConstructor<DICTDELGET>(
    schema = "asm_dictdelget#f462 = DICTDELGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTDELGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTDELGET = DICTDELGET
}
