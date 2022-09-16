package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RANDU256 : AsmInstruction, TlbConstructorProvider<RANDU256> by RANDU256TlbConstructor {
    override fun toString(): String = "RANDU256"
}

private object RANDU256TlbConstructor : TlbConstructor<RANDU256>(
    schema = "asm_randu256#f810 = RANDU256;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RANDU256) {
    }

    override fun loadTlb(cellSlice: CellSlice): RANDU256 = RANDU256
}