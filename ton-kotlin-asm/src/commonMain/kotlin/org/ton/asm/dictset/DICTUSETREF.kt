package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETREF : AsmInstruction, TlbConstructorProvider<DICTUSETREF> by DICTUSETREFTlbConstructor {
    override fun toString(): String = "DICTUSETREF"
}

private object DICTUSETREFTlbConstructor : TlbConstructor<DICTUSETREF>(
    schema = "asm_dictusetref#f417 = DICTUSETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETREF = DICTUSETREF
}
