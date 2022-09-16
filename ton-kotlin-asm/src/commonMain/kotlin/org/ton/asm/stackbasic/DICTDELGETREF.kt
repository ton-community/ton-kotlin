package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTDELGETREF : AsmInstruction, TlbConstructorProvider<DICTDELGETREF> by DICTDELGETREFTlbConstructor {
    override fun toString(): String = "DICTDELGETREF"
}

private object DICTDELGETREFTlbConstructor : TlbConstructor<DICTDELGETREF>(
    schema = "asm_dictdelgetref#f463 = DICTDELGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTDELGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTDELGETREF = DICTDELGETREF
}