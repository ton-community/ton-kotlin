package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SBITREFS : AsmInstruction, TlbConstructorProvider<SBITREFS> by SBITREFSTlbConstructor {
    override fun toString(): String = "SBITREFS"
}

private object SBITREFSTlbConstructor : TlbConstructor<SBITREFS>(
    schema = "asm_sbitrefs#d74b = SBITREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SBITREFS = SBITREFS
}
