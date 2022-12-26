package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ZERO : AsmInstruction, TlbConstructorProvider<ZERO> by ZEROTlbConstructor {
    override fun toString(): String = "0 PUSHINT"
}

private object ZEROTlbConstructor : TlbConstructor<ZERO>(
    schema = "asm_zero#70 = ZERO;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ZERO) {
    }

    override fun loadTlb(cellSlice: CellSlice): ZERO = ZERO
}
