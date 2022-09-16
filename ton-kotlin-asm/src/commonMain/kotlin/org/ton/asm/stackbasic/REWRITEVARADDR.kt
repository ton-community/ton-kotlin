package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REWRITEVARADDR : AsmInstruction, TlbConstructorProvider<REWRITEVARADDR> by REWRITEVARADDRTlbConstructor {
    override fun toString(): String = "REWRITEVARADDR"
}

private object REWRITEVARADDRTlbConstructor : TlbConstructor<REWRITEVARADDR>(
    schema = "asm_rewritevaraddr#fa46 = REWRITEVARADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REWRITEVARADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): REWRITEVARADDR = REWRITEVARADDR
}