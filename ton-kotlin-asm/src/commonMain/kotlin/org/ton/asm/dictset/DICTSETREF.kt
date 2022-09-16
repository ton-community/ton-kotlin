package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETREF : AsmInstruction, TlbConstructorProvider<DICTSETREF> by DICTSETREFTlbConstructor {
    override fun toString(): String = "DICTSETREF"
}

private object DICTSETREFTlbConstructor : TlbConstructor<DICTSETREF>(
    schema = "asm_dictsetref#f413 = DICTSETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETREF = DICTSETREF
}
