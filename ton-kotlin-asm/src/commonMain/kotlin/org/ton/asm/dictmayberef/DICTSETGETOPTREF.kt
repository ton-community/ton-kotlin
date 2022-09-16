package org.ton.asm.dictmayberef

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTSETGETOPTREF> by DICTSETGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTSETGETOPTREF"
}

private object DICTSETGETOPTREFTlbConstructor : TlbConstructor<DICTSETGETOPTREF>(
    schema = "asm_dictsetgetoptref#f46d = DICTSETGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETGETOPTREF = DICTSETGETOPTREF
}
