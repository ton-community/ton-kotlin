package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TWO : AsmInstruction, TlbConstructorProvider<TWO> by TWOTlbConstructor {
    override fun toString(): String = "TWO"
}

private object TWOTlbConstructor : TlbConstructor<TWO>(
    schema = "asm_two#72 = TWO;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TWO) {
    }

    override fun loadTlb(cellSlice: CellSlice): TWO = TWO
}
