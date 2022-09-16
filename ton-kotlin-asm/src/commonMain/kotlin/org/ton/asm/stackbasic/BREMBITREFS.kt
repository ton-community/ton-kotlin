package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BREMBITREFS : AsmInstruction, TlbConstructorProvider<BREMBITREFS> by BREMBITREFSTlbConstructor {
    override fun toString(): String = "BREMBITREFS"
}

private object BREMBITREFSTlbConstructor : TlbConstructor<BREMBITREFS>(
    schema = "asm_brembitrefs#cf37 = BREMBITREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BREMBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BREMBITREFS = BREMBITREFS
}