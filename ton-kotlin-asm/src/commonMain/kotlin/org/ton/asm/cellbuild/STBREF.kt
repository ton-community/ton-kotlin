package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBREF : AsmInstruction, TlbConstructorProvider<STBREF> by STBREFTlbConstructor {
    override fun toString(): String = "STBREF"
}

private object STBREFTlbConstructor : TlbConstructor<STBREF>(
    schema = "asm_stbref#cf11 = STBREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBREF = STBREF
}
