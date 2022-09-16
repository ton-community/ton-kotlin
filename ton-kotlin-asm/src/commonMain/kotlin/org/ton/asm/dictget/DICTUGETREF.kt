package org.ton.asm.dictget

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETREF : AsmInstruction, TlbConstructorProvider<DICTUGETREF> by DICTUGETREFTlbConstructor {
    override fun toString(): String = "DICTUGETREF"
}

private object DICTUGETREFTlbConstructor : TlbConstructor<DICTUGETREF>(
    schema = "asm_dictugetref#f40f = DICTUGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETREF = DICTUGETREF
}
