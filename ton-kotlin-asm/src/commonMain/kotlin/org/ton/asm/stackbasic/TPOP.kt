package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TPOP : AsmInstruction, TlbConstructorProvider<TPOP> by TPOPTlbConstructor {
    override fun toString(): String = "TPOP"
}

private object TPOPTlbConstructor : TlbConstructor<TPOP>(
    schema = "asm_tpop#6f8d = TPOP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TPOP) {
    }

    override fun loadTlb(cellSlice: CellSlice): TPOP = TPOP
}