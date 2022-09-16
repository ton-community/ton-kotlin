package org.ton.asm.dictdelete

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIDELGETREF : AsmInstruction, TlbConstructorProvider<DICTIDELGETREF> by DICTIDELGETREFTlbConstructor {
    override fun toString(): String = "DICTIDELGETREF"
}

private object DICTIDELGETREFTlbConstructor : TlbConstructor<DICTIDELGETREF>(
    schema = "asm_dictidelgetref#f465 = DICTIDELGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIDELGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIDELGETREF = DICTIDELGETREF
}
