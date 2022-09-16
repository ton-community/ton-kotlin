package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIMINREF : AsmInstruction, TlbConstructorProvider<DICTIMINREF> by DICTIMINREFTlbConstructor {
    override fun toString(): String = "DICTIMINREF"
}

private object DICTIMINREFTlbConstructor : TlbConstructor<DICTIMINREF>(
    schema = "asm_dictiminref#f485 = DICTIMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIMINREF = DICTIMINREF
}
