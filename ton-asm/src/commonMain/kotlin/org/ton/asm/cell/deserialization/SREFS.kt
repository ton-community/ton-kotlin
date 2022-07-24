package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SREFS : Instruction, TlbConstructorProvider<SREFS> by SREFSTlbConstructor {
    override fun toString(): String = "SREFS"
}

private object SREFSTlbConstructor : TlbConstructor<SREFS>(
    schema = "asm_srefs#d74a = SREFS;",
    type = SREFS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SREFS = SREFS
}