package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPSFXREV : AsmInstruction, TlbConstructorProvider<SDPSFXREV> by SDPSFXREVTlbConstructor {
    override fun toString(): String = "SDPSFXREV"
}

private object SDPSFXREVTlbConstructor : TlbConstructor<SDPSFXREV>(
    schema = "asm_sdpsfxrev#c70f = SDPSFXREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPSFXREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPSFXREV = SDPSFXREV
}