package org.ton.asm.dictspecial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTGETJMP : AsmInstruction, TlbConstructorProvider<PFXDICTGETJMP> by PFXDICTGETJMPTlbConstructor {
    override fun toString(): String = "PFXDICTGETJMP"
}

private object PFXDICTGETJMPTlbConstructor : TlbConstructor<PFXDICTGETJMP>(
    schema = "asm_pfxdictgetjmp#f4aa = PFXDICTGETJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTGETJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTGETJMP = PFXDICTGETJMP
}
