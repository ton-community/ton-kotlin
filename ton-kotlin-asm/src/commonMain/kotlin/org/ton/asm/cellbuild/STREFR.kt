package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STREFR : AsmInstruction, TlbConstructorProvider<STREFR> by STREFRTlbConstructor {
    override fun toString(): String = "STREFR"
}

private object STREFRTlbConstructor : TlbConstructor<STREFR>(
    schema = "asm_strefr#cf14 = STREFR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREFR) {
    }

    override fun loadTlb(cellSlice: CellSlice): STREFR = STREFR
}
