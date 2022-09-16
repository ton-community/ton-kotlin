package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBR : AsmInstruction, TlbConstructorProvider<STBR> by STBRTlbConstructor {
    override fun toString(): String = "STBR"
}

private object STBRTlbConstructor : TlbConstructor<STBR>(
    schema = "asm_stbr#cf17 = STBR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBR) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBR = STBR
}
