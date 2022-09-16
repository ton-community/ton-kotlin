package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STREFQ : AsmInstruction, TlbConstructorProvider<STREFQ> by STREFQTlbConstructor {
    override fun toString(): String = "STREFQ"
}

private object STREFQTlbConstructor : TlbConstructor<STREFQ>(
    schema = "asm_strefq#cf18 = STREFQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREFQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STREFQ = STREFQ
}