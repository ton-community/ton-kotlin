package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADDGET : AsmInstruction, TlbConstructorProvider<DICTIADDGET> by DICTIADDGETTlbConstructor {
    override fun toString(): String = "DICTIADDGET"
}

private object DICTIADDGETTlbConstructor : TlbConstructor<DICTIADDGET>(
    schema = "asm_dictiaddget#f43c = DICTIADDGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADDGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADDGET = DICTIADDGET
}
