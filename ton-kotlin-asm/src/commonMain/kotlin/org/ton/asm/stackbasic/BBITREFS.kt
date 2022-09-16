package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BBITREFS : AsmInstruction, TlbConstructorProvider<BBITREFS> by BBITREFSTlbConstructor {
    override fun toString(): String = "BBITREFS"
}

private object BBITREFSTlbConstructor : TlbConstructor<BBITREFS>(
    schema = "asm_bbitrefs#cf33 = BBITREFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BBITREFS = BBITREFS
}