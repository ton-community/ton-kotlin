package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPPFX : AsmInstruction, TlbConstructorProvider<SDPPFX> by SDPPFXTlbConstructor {
    override fun toString(): String = "SDPPFX"
}

private object SDPPFXTlbConstructor : TlbConstructor<SDPPFX>(
    schema = "asm_sdppfx#c70a = SDPPFX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPPFX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPPFX = SDPPFX
}