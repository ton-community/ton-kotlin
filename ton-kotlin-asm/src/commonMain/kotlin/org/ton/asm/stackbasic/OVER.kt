package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object OVER : AsmInstruction, TlbConstructorProvider<OVER> by OVERTlbConstructor {
    override fun toString(): String = "OVER"
}

private object OVERTlbConstructor : TlbConstructor<OVER>(
    schema = "asm_over#21 = OVER;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: OVER) {
    }

    override fun loadTlb(cellSlice: CellSlice): OVER = OVER
}
