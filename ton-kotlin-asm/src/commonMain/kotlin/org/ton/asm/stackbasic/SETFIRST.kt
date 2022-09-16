package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETFIRST : AsmInstruction, TlbConstructorProvider<SETFIRST> by SETFIRSTTlbConstructor {
    override fun toString(): String = "SETFIRST"
}

private object SETFIRSTTlbConstructor : TlbConstructor<SETFIRST>(
    schema = "asm_setfirst#6f50 = SETFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETFIRST = SETFIRST
}