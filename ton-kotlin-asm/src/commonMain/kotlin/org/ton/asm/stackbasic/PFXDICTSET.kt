package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTSET : AsmInstruction, TlbConstructorProvider<PFXDICTSET> by PFXDICTSETTlbConstructor {
    override fun toString(): String = "PFXDICTSET"
}

private object PFXDICTSETTlbConstructor : TlbConstructor<PFXDICTSET>(
    schema = "asm_pfxdictset#f470 = PFXDICTSET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTSET) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTSET = PFXDICTSET
}