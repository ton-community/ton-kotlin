package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTUGETOPTREF> by DICTUGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTUGETOPTREF"
}

private object DICTUGETOPTREFTlbConstructor : TlbConstructor<DICTUGETOPTREF>(
    schema = "asm_dictugetoptref#f46b = DICTUGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETOPTREF = DICTUGETOPTREF
}