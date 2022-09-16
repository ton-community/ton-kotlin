package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLSWAPIF2 : AsmInstruction, TlbConstructorProvider<NULLSWAPIF2> by NULLSWAPIF2TlbConstructor {
    override fun toString(): String = "NULLSWAPIF2"
}

private object NULLSWAPIF2TlbConstructor : TlbConstructor<NULLSWAPIF2>(
    schema = "asm_nullswapif2#6fa4 = NULLSWAPIF2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLSWAPIF2) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLSWAPIF2 = NULLSWAPIF2
}