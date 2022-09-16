package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLSWAPIFNOT : AsmInstruction, TlbConstructorProvider<NULLSWAPIFNOT> by NULLSWAPIFNOTTlbConstructor {
    override fun toString(): String = "NULLSWAPIFNOT"
}

private object NULLSWAPIFNOTTlbConstructor : TlbConstructor<NULLSWAPIFNOT>(
    schema = "asm_nullswapifnot#6fa1 = NULLSWAPIFNOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLSWAPIFNOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLSWAPIFNOT = NULLSWAPIFNOT
}