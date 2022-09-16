package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBREFQ : AsmInstruction, TlbConstructorProvider<STBREFQ> by STBREFQTlbConstructor {
    override fun toString(): String = "STBREFQ"
}

private object STBREFQTlbConstructor : TlbConstructor<STBREFQ>(
    schema = "asm_stbrefq#cf19 = STBREFQ;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBREFQ) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBREFQ = STBREFQ
}
