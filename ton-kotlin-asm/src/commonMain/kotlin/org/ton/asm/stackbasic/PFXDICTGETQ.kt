package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PFXDICTGETQ : AsmInstruction, TlbConstructorProvider<PFXDICTGETQ> by PFXDICTGETQTlbConstructor {
    override fun toString(): String = "PFXDICTGETQ"
}

private object PFXDICTGETQTlbConstructor : TlbConstructor<PFXDICTGETQ>(
    schema = "asm_pfxdictgetq#f4a8 = PFXDICTGETQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTGETQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTGETQ = PFXDICTGETQ
}