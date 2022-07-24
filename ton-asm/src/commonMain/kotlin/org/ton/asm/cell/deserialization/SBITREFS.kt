package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SBITREFS : Instruction, TlbConstructorProvider<SBITREFS> by SBITREFSTlbConstructor {
    override fun toString(): String = "SBITREFS"
}

private object SBITREFSTlbConstructor : TlbConstructor<SBITREFS>(
    schema = "asm_sbitrefs#d74b = SBITREFS;",
    type = SBITREFS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SBITREFS = SBITREFS
}