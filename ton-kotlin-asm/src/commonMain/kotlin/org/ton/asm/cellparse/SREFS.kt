package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SREFS : AsmInstruction, TlbConstructorProvider<SREFS> by SREFSTlbConstructor {
    override fun toString(): String = "SREFS"
}

private object SREFSTlbConstructor : TlbConstructor<SREFS>(
    schema = "asm_srefs#d74a = SREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SREFS = SREFS
}
