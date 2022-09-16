package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETGET : AsmInstruction, TlbConstructorProvider<DICTUSETGET> by DICTUSETGETTlbConstructor {
    override fun toString(): String = "DICTUSETGET"
}

private object DICTUSETGETTlbConstructor : TlbConstructor<DICTUSETGET>(
    schema = "asm_dictusetget#f41e = DICTUSETGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETGET = DICTUSETGET
}
