package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDEPTH : Instruction, TlbConstructorProvider<SDEPTH> by SDEPTHTlbConstructor {
    override fun toString(): String = "SDEPTH"
}

private object SDEPTHTlbConstructor : TlbConstructor<SDEPTH>(
    schema = "asm_sdepth#d764 = SDEPTH;",
    type = SDEPTH::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDEPTH = SDEPTH
}