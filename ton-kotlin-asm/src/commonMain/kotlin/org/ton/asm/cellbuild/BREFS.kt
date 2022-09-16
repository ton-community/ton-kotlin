package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BREFS : AsmInstruction, TlbConstructorProvider<BREFS> by BREFSTlbConstructor {
    override fun toString(): String = "BREFS"
}

private object BREFSTlbConstructor : TlbConstructor<BREFS>(
    schema = "asm_brefs#cf32 = BREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BREFS = BREFS
}
