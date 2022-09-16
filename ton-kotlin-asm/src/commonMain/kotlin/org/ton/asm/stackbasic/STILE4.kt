package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STILE4 : AsmInstruction, TlbConstructorProvider<STILE4> by STILE4TlbConstructor {
    override fun toString(): String = "STILE4"
}

private object STILE4TlbConstructor : TlbConstructor<STILE4>(
    schema = "asm_stile4#cf28 = STILE4;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STILE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): STILE4 = STILE4
}