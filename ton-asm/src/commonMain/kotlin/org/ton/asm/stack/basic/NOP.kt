package org.ton.asm.stack.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NOP : Instruction, TlbConstructorProvider<NOP> by NOPTlbConstructor {
    override fun toString(): String = "NOP"
}

private object NOPTlbConstructor : TlbConstructor<NOP>(
    schema = "asm_nop#00 = NOP;",
    type = NOP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOP) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOP = NOP
}