package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STDICTS : AsmInstruction, TlbConstructorProvider<STDICTS> by STDICTSTlbConstructor {
    override fun toString(): String = "STDICTS"
}

private object STDICTSTlbConstructor : TlbConstructor<STDICTS>(
    schema = "asm_stdicts#ce = STDICTS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STDICTS) {
    }

    override fun loadTlb(cellSlice: CellSlice): STDICTS = STDICTS
}