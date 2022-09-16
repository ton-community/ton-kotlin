package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLSWAPIFNOT2 : AsmInstruction, TlbConstructorProvider<NULLSWAPIFNOT2> by NULLSWAPIFNOT2TlbConstructor {
    override fun toString(): String = "NULLSWAPIFNOT2"
}

private object NULLSWAPIFNOT2TlbConstructor : TlbConstructor<NULLSWAPIFNOT2>(
    schema = "asm_nullswapifnot2#6fa5 = NULLSWAPIFNOT2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLSWAPIFNOT2) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLSWAPIFNOT2 = NULLSWAPIFNOT2
}
