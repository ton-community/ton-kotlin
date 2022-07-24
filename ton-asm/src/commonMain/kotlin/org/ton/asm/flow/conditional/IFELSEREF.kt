package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFELSEREF : Instruction, TlbConstructorProvider<IFELSEREF> by IFELSEREFTLbConstructor {
    override fun toString(): String = "IFELSEREF"
}

private object IFELSEREFTLbConstructor : TlbConstructor<IFELSEREF>(
    schema = "asm_ifelseref#e30e = IFELSEREF;",
    type = IFELSEREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFELSEREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFELSEREF = IFELSEREF
}