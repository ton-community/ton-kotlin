package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STONE : AsmInstruction, TlbConstructorProvider<STONE> by STONETlbConstructor {
    override fun toString(): String = "STONE"
}

private object STONETlbConstructor : TlbConstructor<STONE>(
    schema = "asm_stone#cf83 = STONE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STONE) {
    }

    override fun loadTlb(cellSlice: CellSlice): STONE = STONE
}