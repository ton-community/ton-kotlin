package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPFX : AsmInstruction, TlbConstructorProvider<SDPFX> by SDPFXTlbConstructor {
    override fun toString(): String = "SDPFX"
}

private object SDPFXTlbConstructor : TlbConstructor<SDPFX>(
    schema = "asm_sdpfx#c708 = SDPFX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPFX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPFX = SDPFX
}