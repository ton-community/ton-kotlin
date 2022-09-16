package org.ton.asm.compareother

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCNTTRAIL0 : AsmInstruction, TlbConstructorProvider<SDCNTTRAIL0> by SDCNTTRAIL0TlbConstructor {
    override fun toString(): String = "SDCNTTRAIL0"
}

private object SDCNTTRAIL0TlbConstructor : TlbConstructor<SDCNTTRAIL0>(
    schema = "asm_sdcnttrail0#c712 = SDCNTTRAIL0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCNTTRAIL0) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCNTTRAIL0 = SDCNTTRAIL0
}
