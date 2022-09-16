package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISET : AsmInstruction, TlbConstructorProvider<DICTISET> by DICTISETTlbConstructor {
    override fun toString(): String = "DICTISET"
}

private object DICTISETTlbConstructor : TlbConstructor<DICTISET>(
    schema = "asm_dictiset#f414 = DICTISET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISET = DICTISET
}
