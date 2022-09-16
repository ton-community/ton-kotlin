package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCUTFIRST : AsmInstruction, TlbConstructorProvider<SDCUTFIRST> by SDCUTFIRSTTlbConstructor {
    override fun toString(): String = "SDCUTFIRST"
}

private object SDCUTFIRSTTlbConstructor : TlbConstructor<SDCUTFIRST>(
    schema = "asm_sdcutfirst#d720 = SDCUTFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCUTFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCUTFIRST = SDCUTFIRST
}