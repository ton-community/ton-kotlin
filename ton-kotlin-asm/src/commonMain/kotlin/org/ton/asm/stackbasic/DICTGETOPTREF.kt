package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETOPTREF : AsmInstruction, TlbConstructorProvider<DICTGETOPTREF> by DICTGETOPTREFTlbConstructor {
    override fun toString(): String = "DICTGETOPTREF"
}

private object DICTGETOPTREFTlbConstructor : TlbConstructor<DICTGETOPTREF>(
    schema = "asm_dictgetoptref#f469 = DICTGETOPTREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETOPTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETOPTREF = DICTGETOPTREF
}