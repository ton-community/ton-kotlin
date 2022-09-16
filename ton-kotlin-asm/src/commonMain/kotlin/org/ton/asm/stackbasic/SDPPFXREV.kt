package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPPFXREV : AsmInstruction, TlbConstructorProvider<SDPPFXREV> by SDPPFXREVTlbConstructor {
    override fun toString(): String = "SDPPFXREV"
}

private object SDPPFXREVTlbConstructor : TlbConstructor<SDPPFXREV>(
    schema = "asm_sdppfxrev#c70b = SDPPFXREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPPFXREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPPFXREV = SDPPFXREV
}