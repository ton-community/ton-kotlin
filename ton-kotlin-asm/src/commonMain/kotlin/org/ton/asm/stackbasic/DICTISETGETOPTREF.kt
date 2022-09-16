package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTISETGETOPTREF> by DICTISETGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTISETGETOPTREF"
}

private object DICTISETGETOPTREFTlbConstructor : TlbConstructor<DICTISETGETOPTREF>(
    schema = "asm_dictisetgetoptref#f46e = DICTISETGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETGETOPTREF = DICTISETGETOPTREF
}