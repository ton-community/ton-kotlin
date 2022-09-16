package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETGET : AsmInstruction, TlbConstructorProvider<DICTISETGET> by DICTISETGETTlbConstructor {
    override fun toString(): String = "DICTISETGET"
}

private object DICTISETGETTlbConstructor : TlbConstructor<DICTISETGET>(
    schema = "asm_dictisetget#f41c = DICTISETGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETGET = DICTISETGET
}
