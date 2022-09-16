package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BCHKBITREFS : AsmInstruction, TlbConstructorProvider<BCHKBITREFS> by BCHKBITREFSTlbConstructor {
    override fun toString(): String = "BCHKBITREFS"
}

private object BCHKBITREFSTlbConstructor : TlbConstructor<BCHKBITREFS>(
    schema = "asm_bchkbitrefs#cf3b = BCHKBITREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BCHKBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BCHKBITREFS = BCHKBITREFS
}
