package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPSFX : AsmInstruction, TlbConstructorProvider<SDPSFX> by SDPSFXTlbConstructor {
    override fun toString(): String = "SDPSFX"
}

private object SDPSFXTlbConstructor : TlbConstructor<SDPSFX>(
    schema = "asm_sdpsfx#c70e = SDPSFX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPSFX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPSFX = SDPSFX
}