package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTADD : AsmInstruction, TlbConstructorProvider<PFXDICTADD> by PFXDICTADDTlbConstructor {
    override fun toString(): String = "PFXDICTADD"
}

private object PFXDICTADDTlbConstructor : TlbConstructor<PFXDICTADD>(
    schema = "asm_pfxdictadd#f472 = PFXDICTADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTADD = PFXDICTADD
}