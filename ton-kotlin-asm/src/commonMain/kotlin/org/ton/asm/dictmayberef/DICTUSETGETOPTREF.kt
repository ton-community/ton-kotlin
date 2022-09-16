package org.ton.asm.dictmayberef

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTUSETGETOPTREF> by DICTUSETGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTUSETGETOPTREF"
}

private object DICTUSETGETOPTREFTlbConstructor : TlbConstructor<DICTUSETGETOPTREF>(
    schema = "asm_dictusetgetoptref#f46f = DICTUSETGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETGETOPTREF = DICTUSETGETOPTREF
}
