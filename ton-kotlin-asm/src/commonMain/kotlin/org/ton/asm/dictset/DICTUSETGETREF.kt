package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETGETREF : AsmInstruction, TlbConstructorProvider<DICTUSETGETREF> by DICTUSETGETREFTlbConstructor {
    override fun toString(): String = "DICTUSETGETREF"
}

private object DICTUSETGETREFTlbConstructor : TlbConstructor<DICTUSETGETREF>(
    schema = "asm_dictusetgetref#f41f = DICTUSETGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETGETREF = DICTUSETGETREF
}
