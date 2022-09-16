package org.ton.asm.compareother

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCNTLEAD1 : AsmInstruction, TlbConstructorProvider<SDCNTLEAD1> by SDCNTLEAD1TlbConstructor {
    override fun toString(): String = "SDCNTLEAD1"
}

private object SDCNTLEAD1TlbConstructor : TlbConstructor<SDCNTLEAD1>(
    schema = "asm_sdcntlead1#c711 = SDCNTLEAD1;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCNTLEAD1) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCNTLEAD1 = SDCNTLEAD1
}
