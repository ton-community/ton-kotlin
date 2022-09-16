package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTREMMINREF : AsmInstruction, TlbConstructorProvider<DICTREMMINREF> by DICTREMMINREFTlbConstructor {
    override fun toString(): String = "DICTREMMINREF"
}

private object DICTREMMINREFTlbConstructor : TlbConstructor<DICTREMMINREF>(
    schema = "asm_dictremminref#f493 = DICTREMMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTREMMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTREMMINREF = DICTREMMINREF
}
