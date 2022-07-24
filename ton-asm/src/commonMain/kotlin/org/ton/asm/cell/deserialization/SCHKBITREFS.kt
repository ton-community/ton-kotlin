package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKBITREFS : Instruction, TlbConstructorProvider<SCHKBITREFS> by SCHKBITREFSTlbConstructor {
    override fun toString(): String = "SCHKBITREFS"
}

private object SCHKBITREFSTlbConstructor : TlbConstructor<SCHKBITREFS>(
    schema = "asm_schkbitrefs#d743 = SCHKBITREFS;",
    type = SCHKBITREFS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKBITREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKBITREFS = SCHKBITREFS
}