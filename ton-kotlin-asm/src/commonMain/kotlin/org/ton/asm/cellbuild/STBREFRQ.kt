package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBREFRQ : AsmInstruction, TlbConstructorProvider<STBREFRQ> by STBREFRQTlbConstructor {
    override fun toString(): String = "STBREFRQ"
}

private object STBREFRQTlbConstructor : TlbConstructor<STBREFRQ>(
    schema = "asm_stbrefrq#cf1d = STBREFRQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBREFRQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBREFRQ = STBREFRQ
}
