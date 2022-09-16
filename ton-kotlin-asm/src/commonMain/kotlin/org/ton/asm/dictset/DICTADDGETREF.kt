package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADDGETREF : AsmInstruction, TlbConstructorProvider<DICTADDGETREF> by DICTADDGETREFTlbConstructor {
    override fun toString(): String = "DICTADDGETREF"
}

private object DICTADDGETREFTlbConstructor : TlbConstructor<DICTADDGETREF>(
    schema = "asm_dictaddgetref#f43b = DICTADDGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADDGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADDGETREF = DICTADDGETREF
}
