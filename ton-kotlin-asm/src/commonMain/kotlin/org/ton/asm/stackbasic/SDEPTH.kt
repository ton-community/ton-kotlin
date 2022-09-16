package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDEPTH : AsmInstruction, TlbConstructorProvider<SDEPTH> by SDEPTHTlbConstructor {
    override fun toString(): String = "SDEPTH"
}

private object SDEPTHTlbConstructor : TlbConstructor<SDEPTH>(
    schema = "asm_sdepth#d764 = SDEPTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDEPTH = SDEPTH
}