package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSET : AsmInstruction, TlbConstructorProvider<DICTSET> by DICTSETTlbConstructor {
    override fun toString(): String = "DICTSET"
}

private object DICTSETTlbConstructor : TlbConstructor<DICTSET>(
    schema = "asm_dictset#f412 = DICTSET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSET = DICTSET
}
