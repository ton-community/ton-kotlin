package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTIGETOPTREF> by DICTIGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTIGETOPTREF"
}

private object DICTIGETOPTREFTlbConstructor : TlbConstructor<DICTIGETOPTREF>(
    schema = "asm_dictigetoptref#f46a = DICTIGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETOPTREF = DICTIGETOPTREF
}