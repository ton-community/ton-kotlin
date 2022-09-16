package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SINGLE : AsmInstruction, TlbConstructorProvider<SINGLE> by SINGLETlbConstructor {
    override fun toString(): String = "SINGLE"
}

private object SINGLETlbConstructor : TlbConstructor<SINGLE>(
    schema = "asm_single#6f01 = SINGLE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SINGLE) {
    }

    override fun loadTlb(cellSlice: CellSlice): SINGLE = SINGLE
}