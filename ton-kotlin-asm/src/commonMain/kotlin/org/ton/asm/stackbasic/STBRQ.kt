package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBRQ : AsmInstruction, TlbConstructorProvider<STBRQ> by STBRQTlbConstructor {
    override fun toString(): String = "STBRQ"
}

private object STBRQTlbConstructor : TlbConstructor<STBRQ>(
    schema = "asm_stbrq#cf1f = STBRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBRQ = STBRQ
}