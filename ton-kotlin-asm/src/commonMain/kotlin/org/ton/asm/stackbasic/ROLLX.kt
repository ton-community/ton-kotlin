package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ROLLX : AsmInstruction, TlbConstructorProvider<ROLLX> by ROLLXTlbConstructor {
    override fun toString(): String = "ROLLX"
}

private object ROLLXTlbConstructor : TlbConstructor<ROLLX>(
    schema = "asm_rollx#61 = ROLLX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROLLX) {
    }

    override fun loadTlb(cellSlice: CellSlice): ROLLX = ROLLX
}