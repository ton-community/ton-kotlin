package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BREMREFS : AsmInstruction, TlbConstructorProvider<BREMREFS> by BREMREFSTlbConstructor {
    override fun toString(): String = "BREMREFS"
}

private object BREMREFSTlbConstructor : TlbConstructor<BREMREFS>(
    schema = "asm_bremrefs#cf36 = BREMREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BREMREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BREMREFS = BREMREFS
}
