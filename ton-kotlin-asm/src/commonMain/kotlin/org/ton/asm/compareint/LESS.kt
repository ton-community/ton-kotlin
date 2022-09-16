package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LESS : AsmInstruction, TlbConstructorProvider<LESS> by LESSTlbConstructor {
    override fun toString(): String = "LESS"
}

private object LESSTlbConstructor : TlbConstructor<LESS>(
    schema = "asm_less#b9 = LESS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LESS) {
    }

    override fun loadTlb(cellSlice: CellSlice): LESS = LESS
}
