package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SECONDQ : AsmInstruction, TlbConstructorProvider<SECONDQ> by SECONDQTlbConstructor {
    override fun toString(): String = "SECONDQ"
}

private object SECONDQTlbConstructor : TlbConstructor<SECONDQ>(
    schema = "asm_secondq#6f61 = SECONDQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SECONDQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): SECONDQ = SECONDQ
}