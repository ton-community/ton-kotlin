package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIDELGET : AsmInstruction, TlbConstructorProvider<DICTIDELGET> by DICTIDELGETTlbConstructor {
    override fun toString(): String = "DICTIDELGET"
}

private object DICTIDELGETTlbConstructor : TlbConstructor<DICTIDELGET>(
    schema = "asm_dictidelget#f464 = DICTIDELGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIDELGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIDELGET = DICTIDELGET
}
