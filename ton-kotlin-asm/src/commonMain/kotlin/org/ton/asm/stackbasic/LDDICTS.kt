package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LDDICTS : AsmInstruction, TlbConstructorProvider<LDDICTS> by LDDICTSTlbConstructor {
    override fun toString(): String = "LDDICTS"
}

private object LDDICTSTlbConstructor : TlbConstructor<LDDICTS>(
    schema = "asm_lddicts#f402 = LDDICTS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LDDICTS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LDDICTS = LDDICTS
}