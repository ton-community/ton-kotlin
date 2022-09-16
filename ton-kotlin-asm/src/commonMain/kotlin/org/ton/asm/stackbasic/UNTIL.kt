package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNTIL : AsmInstruction, TlbConstructorProvider<UNTIL> by UNTILTlbConstructor {
    override fun toString(): String = "UNTIL"
}

private object UNTILTlbConstructor : TlbConstructor<UNTIL>(
    schema = "asm_until#e6 = UNTIL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNTIL) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNTIL = UNTIL
}