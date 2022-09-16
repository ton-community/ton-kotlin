package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BDEPTH : AsmInstruction, TlbConstructorProvider<BDEPTH> by BDEPTHTlbConstructor {
    override fun toString(): String = "BDEPTH"
}

private object BDEPTHTlbConstructor : TlbConstructor<BDEPTH>(
    schema = "asm_bdepth#cf30 = BDEPTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): BDEPTH = BDEPTH
}
