package org.ton.asm.compareother

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCNTLEAD0 : AsmInstruction, TlbConstructorProvider<SDCNTLEAD0> by SDCNTLEAD0TlbConstructor {
    override fun toString(): String = "SDCNTLEAD0"
}

private object SDCNTLEAD0TlbConstructor : TlbConstructor<SDCNTLEAD0>(
    schema = "asm_sdcntlead0#c710 = SDCNTLEAD0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCNTLEAD0) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCNTLEAD0 = SDCNTLEAD0
}
