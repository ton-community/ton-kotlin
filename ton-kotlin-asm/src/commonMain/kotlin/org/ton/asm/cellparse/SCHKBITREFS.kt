package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITREFS : AsmInstruction, TlbConstructorProvider<SCHKBITREFS> by SCHKBITREFSTlbConstructor {
    override fun toString(): String = "SCHKBITREFS"
}

private object SCHKBITREFSTlbConstructor : TlbConstructor<SCHKBITREFS>(
    schema = "asm_schkbitrefs#d743 = SCHKBITREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITREFS = SCHKBITREFS
}
