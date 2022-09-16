package org.ton.asm.dictspecial

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTGETEXEC : AsmInstruction, TlbConstructorProvider<PFXDICTGETEXEC> by PFXDICTGETEXECTlbConstructor {
    override fun toString(): String = "PFXDICTGETEXEC"
}

private object PFXDICTGETEXECTlbConstructor : TlbConstructor<PFXDICTGETEXEC>(
    schema = "asm_pfxdictgetexec#f4ab = PFXDICTGETEXEC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTGETEXEC) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTGETEXEC = PFXDICTGETEXEC
}
