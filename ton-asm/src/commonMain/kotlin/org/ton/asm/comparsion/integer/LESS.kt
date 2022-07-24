package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LESS : Instruction, TlbConstructorProvider<LESS> by LESSTlbConstructor {
    override fun toString(): String = "LESS"
}

private object LESSTlbConstructor : TlbConstructor<LESS>(
    schema = "asm_less#b9 = LESS;",
    type = LESS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LESS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LESS = LESS
}