package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCNTTRAIL1 : AsmInstruction, TlbConstructorProvider<SDCNTTRAIL1> by SDCNTTRAIL1TlbConstructor {
    override fun toString(): String = "SDCNTTRAIL1"
}

private object SDCNTTRAIL1TlbConstructor : TlbConstructor<SDCNTTRAIL1>(
    schema = "asm_sdcnttrail1#c713 = SDCNTTRAIL1;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCNTTRAIL1) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCNTTRAIL1 = SDCNTTRAIL1
}