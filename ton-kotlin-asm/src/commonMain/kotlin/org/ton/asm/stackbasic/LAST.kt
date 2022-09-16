package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object LAST : AsmInstruction, TlbConstructorProvider<LAST> by LASTTlbConstructor {
    override fun toString(): String = "LAST"
}

private object LASTTlbConstructor : TlbConstructor<LAST>(
    schema = "asm_last#6f8b = LAST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): LAST = LAST
}