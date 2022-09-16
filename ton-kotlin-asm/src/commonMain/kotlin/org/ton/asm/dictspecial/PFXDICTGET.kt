package org.ton.asm.dictspecial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTGET : AsmInstruction, TlbConstructorProvider<PFXDICTGET> by PFXDICTGETTlbConstructor {
    override fun toString(): String = "PFXDICTGET"
}

private object PFXDICTGETTlbConstructor : TlbConstructor<PFXDICTGET>(
    schema = "asm_pfxdictget#f4a9 = PFXDICTGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTGET = PFXDICTGET
}
