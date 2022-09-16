package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREMMINREF : AsmInstruction, TlbConstructorProvider<DICTIREMMINREF> by DICTIREMMINREFTlbConstructor {
    override fun toString(): String = "DICTIREMMINREF"
}

private object DICTIREMMINREFTlbConstructor : TlbConstructor<DICTIREMMINREF>(
    schema = "asm_dictiremminref#f495 = DICTIREMMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREMMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREMMINREF = DICTIREMMINREF
}