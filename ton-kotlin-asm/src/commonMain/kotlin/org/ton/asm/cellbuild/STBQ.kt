package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBQ : AsmInstruction, TlbConstructorProvider<STBQ> by STBQTlbConstructor {
    override fun toString(): String = "STBQ"
}

private object STBQTlbConstructor : TlbConstructor<STBQ>(
    schema = "asm_stbq#cf1b = STBQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBQ = STBQ
}
