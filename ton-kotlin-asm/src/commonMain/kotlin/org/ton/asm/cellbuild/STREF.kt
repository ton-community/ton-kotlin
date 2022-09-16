package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STREF : AsmInstruction, TlbConstructorProvider<STREF> by STREFTlbConstructor {
    override fun toString(): String = "STREF"
}

private object STREFTlbConstructor : TlbConstructor<STREF>(
    schema = "asm_stref#cc = STREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): STREF = STREF
}
