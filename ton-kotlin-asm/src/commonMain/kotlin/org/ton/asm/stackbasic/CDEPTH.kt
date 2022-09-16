package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDEPTH : AsmInstruction, TlbConstructorProvider<CDEPTH> by CDEPTHTlbConstructor {
    override fun toString(): String = "CDEPTH"
}

private object CDEPTHTlbConstructor : TlbConstructor<CDEPTH>(
    schema = "asm_cdepth#d765 = CDEPTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDEPTH = CDEPTH
}