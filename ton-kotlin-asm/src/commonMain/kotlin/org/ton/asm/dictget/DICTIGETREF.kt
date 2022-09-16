package org.ton.asm.dictget

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETREF : AsmInstruction, TlbConstructorProvider<DICTIGETREF> by DICTIGETREFTlbConstructor {
    override fun toString(): String = "DICTIGETREF"
}

private object DICTIGETREFTlbConstructor : TlbConstructor<DICTIGETREF>(
    schema = "asm_dictigetref#f40d = DICTIGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETREF = DICTIGETREF
}
