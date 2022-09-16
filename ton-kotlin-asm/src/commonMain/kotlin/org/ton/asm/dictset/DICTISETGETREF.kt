package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETGETREF : AsmInstruction, TlbConstructorProvider<DICTISETGETREF> by DICTISETGETREFTlbConstructor {
    override fun toString(): String = "DICTISETGETREF"
}

private object DICTISETGETREFTlbConstructor : TlbConstructor<DICTISETGETREF>(
    schema = "asm_dictisetgetref#f41d = DICTISETGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETGETREF = DICTISETGETREF
}
