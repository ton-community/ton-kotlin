package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSFXREV : AsmInstruction, TlbConstructorProvider<SDSFXREV> by SDSFXREVTlbConstructor {
    override fun toString(): String = "SDSFXREV"
}

private object SDSFXREVTlbConstructor : TlbConstructor<SDSFXREV>(
    schema = "asm_sdsfxrev#c70d = SDSFXREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSFXREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSFXREV = SDSFXREV
}