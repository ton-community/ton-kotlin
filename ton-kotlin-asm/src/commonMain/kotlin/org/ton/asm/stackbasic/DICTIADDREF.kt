package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADDREF : AsmInstruction, TlbConstructorProvider<DICTIADDREF> by DICTIADDREFTlbConstructor {
    override fun toString(): String = "DICTIADDREF"
}

private object DICTIADDREFTlbConstructor : TlbConstructor<DICTIADDREF>(
    schema = "asm_dictiaddref#f435 = DICTIADDREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADDREF = DICTIADDREF
}