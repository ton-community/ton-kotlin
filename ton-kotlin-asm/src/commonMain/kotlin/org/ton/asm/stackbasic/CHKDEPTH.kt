package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CHKDEPTH : AsmInstruction, TlbConstructorProvider<CHKDEPTH> by CHKDEPTHTlbConstructor {
    override fun toString(): String = "CHKDEPTH"
}

private object CHKDEPTHTlbConstructor : TlbConstructor<CHKDEPTH>(
    schema = "asm_chkdepth#69 = CHKDEPTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CHKDEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): CHKDEPTH = CHKDEPTH
}