package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MIN : Instruction, TlbConstructorProvider<MIN> by MINTlbConstructor {
    override fun toString(): String = "MIN"
}

private object MINTlbConstructor : TlbConstructor<MIN>(
    schema = "asm_min#b608 = MIN;",
    type = MIN::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): MIN = MIN
}