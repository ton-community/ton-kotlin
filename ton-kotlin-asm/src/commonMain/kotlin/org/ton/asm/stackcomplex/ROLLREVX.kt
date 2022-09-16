package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ROLLREVX : AsmInstruction, TlbConstructorProvider<ROLLREVX> by ROLLREVXTlbConstructor {
    override fun toString(): String = "ROLLREVX"
}

private object ROLLREVXTlbConstructor : TlbConstructor<ROLLREVX>(
    schema = "asm_rollrevx#62 = ROLLREVX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROLLREVX) {
    }

    override fun loadTlb(cellSlice: CellSlice): ROLLREVX = ROLLREVX
}
