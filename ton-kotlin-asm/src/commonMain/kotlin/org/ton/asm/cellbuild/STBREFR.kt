package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STBREFR : AsmInstruction, TlbConstructorProvider<STBREFR> by STBREFRTlbConstructor {
    override fun toString(): String = "STBREFR"
}

private object STBREFRTlbConstructor : TlbConstructor<STBREFR>(
    schema = "asm_stbrefr#cd = STBREFR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STBREFR) {
    }

    override fun loadTlb(cellSlice: CellSlice): STBREFR = STBREFR
}
