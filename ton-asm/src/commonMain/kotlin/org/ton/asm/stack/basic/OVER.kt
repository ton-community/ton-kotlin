package org.ton.asm.stack.basic

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object OVER : Instruction, TlbConstructorProvider<OVER> by OVERTlbConstructor {
    override fun toString(): String = "OVER"
}

private object OVERTlbConstructor : TlbConstructor<OVER>(
    schema = "asm_over#21 = OVER;",
    type = OVER::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: OVER) {
    }

    override fun loadTlb(cellSlice: CellSlice): OVER = OVER
}