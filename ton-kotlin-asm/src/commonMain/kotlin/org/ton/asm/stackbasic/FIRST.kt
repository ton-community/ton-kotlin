package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object FIRST : AsmInstruction, TlbConstructorProvider<FIRST> by FIRSTTlbConstructor {
    override fun toString(): String = "FIRST"
}

private object FIRSTTlbConstructor : TlbConstructor<FIRST>(
    schema = "asm_first#6f10 = FIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): FIRST = FIRST
}