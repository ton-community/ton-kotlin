package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STIXRQ : AsmInstruction, TlbConstructorProvider<STIXRQ> by STIXRQTlbConstructor {
    override fun toString(): String = "STIXRQ"
}

private object STIXRQTlbConstructor : TlbConstructor<STIXRQ>(
    schema = "asm_stixrq#cf06 = STIXRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIXRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STIXRQ = STIXRQ
}
