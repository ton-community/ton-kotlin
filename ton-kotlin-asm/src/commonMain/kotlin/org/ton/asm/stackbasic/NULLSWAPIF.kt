package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLSWAPIF : AsmInstruction, TlbConstructorProvider<NULLSWAPIF> by NULLSWAPIFTlbConstructor {
    override fun toString(): String = "NULLSWAPIF"
}

private object NULLSWAPIFTlbConstructor : TlbConstructor<NULLSWAPIF>(
    schema = "asm_nullswapif#6fa0 = NULLSWAPIF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLSWAPIF) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLSWAPIF = NULLSWAPIF
}