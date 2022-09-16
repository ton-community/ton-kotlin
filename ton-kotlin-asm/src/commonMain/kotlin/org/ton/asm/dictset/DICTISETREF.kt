package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETREF : AsmInstruction, TlbConstructorProvider<DICTISETREF> by DICTISETREFTlbConstructor {
    override fun toString(): String = "DICTISETREF"
}

private object DICTISETREFTlbConstructor : TlbConstructor<DICTISETREF>(
    schema = "asm_dictisetref#f415 = DICTISETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETREF = DICTISETREF
}
