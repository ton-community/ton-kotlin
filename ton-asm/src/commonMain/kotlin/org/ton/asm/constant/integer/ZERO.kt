package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ZERO : Instruction, TlbConstructorProvider<ZERO> by ZEROTlbConstructor {
    override fun toString(): String = "ZERO"
}

private object ZEROTlbConstructor : TlbConstructor<ZERO>(
    schema = "asm_zero#70 = ZERO;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ZERO) {
    }

    override fun loadTlb(cellSlice: CellSlice): ZERO = ZERO
}