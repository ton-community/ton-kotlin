package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSFX : AsmInstruction, TlbConstructorProvider<SDSFX> by SDSFXTlbConstructor {
    override fun toString(): String = "SDSFX"
}

private object SDSFXTlbConstructor : TlbConstructor<SDSFX>(
    schema = "asm_sdsfx#c70c = SDSFX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSFX) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSFX = SDSFX
}