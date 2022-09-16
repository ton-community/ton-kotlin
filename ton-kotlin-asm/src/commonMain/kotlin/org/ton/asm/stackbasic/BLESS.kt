package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BLESS : AsmInstruction, TlbConstructorProvider<BLESS> by BLESSTlbConstructor {
    override fun toString(): String = "BLESS"
}

private object BLESSTlbConstructor : TlbConstructor<BLESS>(
    schema = "asm_bless#ed1e = BLESS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLESS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BLESS = BLESS
}