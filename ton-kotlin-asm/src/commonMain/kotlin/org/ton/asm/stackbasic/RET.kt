package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RET : AsmInstruction, TlbConstructorProvider<RET> by RETTlbConstructor {
    override fun toString(): String = "RET"
}

private object RETTlbConstructor : TlbConstructor<RET>(
    schema = "asm_ret#db30 = RET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RET) {
    }

    override fun loadTlb(cellSlice: CellSlice): RET = RET
}