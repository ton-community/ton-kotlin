package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STIXQ : AsmInstruction, TlbConstructorProvider<STIXQ> by STIXQTlbConstructor {
    override fun toString(): String = "STIXQ"
}

private object STIXQTlbConstructor : TlbConstructor<STIXQ>(
    schema = "asm_stixq#cf04 = STIXQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STIXQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STIXQ = STIXQ
}
