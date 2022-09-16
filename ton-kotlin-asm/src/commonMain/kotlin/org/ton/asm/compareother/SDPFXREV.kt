package org.ton.asm.compareother

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDPFXREV : AsmInstruction, TlbConstructorProvider<SDPFXREV> by SDPFXREVTlbConstructor {
    override fun toString(): String = "SDPFXREV"
}

private object SDPFXREVTlbConstructor : TlbConstructor<SDPFXREV>(
    schema = "asm_sdpfxrev#c709 = SDPFXREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDPFXREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDPFXREV = SDPFXREV
}
