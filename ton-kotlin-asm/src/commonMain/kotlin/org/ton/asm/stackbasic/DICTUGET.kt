package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGET : AsmInstruction, TlbConstructorProvider<DICTUGET> by DICTUGETTlbConstructor {
    override fun toString(): String = "DICTUGET"
}

private object DICTUGETTlbConstructor : TlbConstructor<DICTUGET>(
    schema = "asm_dictuget#f40e = DICTUGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGET = DICTUGET
}