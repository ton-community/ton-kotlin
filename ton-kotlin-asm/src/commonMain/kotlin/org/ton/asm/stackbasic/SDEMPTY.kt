package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDEMPTY : AsmInstruction, TlbConstructorProvider<SDEMPTY> by SDEMPTYTlbConstructor {
    override fun toString(): String = "SDEMPTY"
}

private object SDEMPTYTlbConstructor : TlbConstructor<SDEMPTY>(
    schema = "asm_sdempty#c701 = SDEMPTY;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDEMPTY) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDEMPTY = SDEMPTY
}