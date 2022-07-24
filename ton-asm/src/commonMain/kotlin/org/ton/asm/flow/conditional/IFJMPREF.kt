package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFJMPREF : Instruction, TlbConstructorProvider<IFJMPREF> by IFJMPREFTlbConstructor {
    override fun toString(): String = "IFJMPREF"
}

private object IFJMPREFTlbConstructor : TlbConstructor<IFJMPREF>(
    schema = "asm_ifjmpref#e302 = IFJMPREF;",
    type = IFJMPREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFJMPREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFJMPREF = IFJMPREF
}