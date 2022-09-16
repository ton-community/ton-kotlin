package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADDGETREF : AsmInstruction, TlbConstructorProvider<DICTIADDGETREF> by DICTIADDGETREFTlbConstructor {
    override fun toString(): String = "DICTIADDGETREF"
}

private object DICTIADDGETREFTlbConstructor : TlbConstructor<DICTIADDGETREF>(
    schema = "asm_dictiaddgetref#f43d = DICTIADDGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADDGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADDGETREF = DICTIADDGETREF
}
