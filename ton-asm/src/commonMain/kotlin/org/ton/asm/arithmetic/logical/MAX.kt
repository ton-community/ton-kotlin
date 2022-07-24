package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MAX : Instruction, TlbConstructorProvider<MAX> by MAXTlbConstructor {
    override fun toString(): String = "MAX"
}

private object MAXTlbConstructor : TlbConstructor<MAX>(
    schema = "asm_max#b609 = MAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): MAX = MAX
}