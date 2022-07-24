package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TWO : Instruction, TlbConstructorProvider<TWO> by TWOTlbConstructor {
    override fun toString(): String = "TWO"
}

private object TWOTlbConstructor : TlbConstructor<TWO>(
    schema = "asm_two#72 = TWO;",
    type = TWO::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TWO) {
    }

    override fun loadTlb(cellSlice: CellSlice): TWO = TWO
}