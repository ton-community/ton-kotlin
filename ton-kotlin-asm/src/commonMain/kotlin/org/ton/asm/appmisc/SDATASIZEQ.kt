package org.ton.asm.appmisc

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDATASIZEQ : AsmInstruction, TlbConstructorProvider<SDATASIZEQ> by SDATASIZEQTlbConstructor {
    override fun toString(): String = "SDATASIZEQ"
}

private object SDATASIZEQTlbConstructor : TlbConstructor<SDATASIZEQ>(
    schema = "asm_sdatasizeq#f942 = SDATASIZEQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDATASIZEQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDATASIZEQ = SDATASIZEQ
}
