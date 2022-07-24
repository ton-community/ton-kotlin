package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTJMPREF : Instruction, TlbConstructorProvider<IFNOTJMPREF> by IFNOTJMPREFTlbConstructor {
    override fun toString(): String = "IFNOTJMPREF"
}

private object IFNOTJMPREFTlbConstructor : TlbConstructor<IFNOTJMPREF>(
    schema = "asm_ifnotjmpref#e303 = IFNOTJMPREF;",
    type = IFNOTJMPREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTJMPREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTJMPREF = IFNOTJMPREF
}