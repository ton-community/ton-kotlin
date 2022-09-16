package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTDEL : AsmInstruction, TlbConstructorProvider<PFXDICTDEL> by PFXDICTDELTlbConstructor {
    override fun toString(): String = "PFXDICTDEL"
}

private object PFXDICTDELTlbConstructor : TlbConstructor<PFXDICTDEL>(
    schema = "asm_pfxdictdel#f473 = PFXDICTDEL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTDEL) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTDEL = PFXDICTDEL
}