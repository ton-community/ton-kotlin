package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STREFRQ : AsmInstruction, TlbConstructorProvider<STREFRQ> by STREFRQTlbConstructor {
    override fun toString(): String = "STREFRQ"
}

private object STREFRQTlbConstructor : TlbConstructor<STREFRQ>(
    schema = "asm_strefrq#cf1c = STREFRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREFRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STREFRQ = STREFRQ
}
