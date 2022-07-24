package org.ton.asm.flow.conditional

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFREFELSEREF : Instruction, TlbConstructorProvider<IFREFELSEREF> by IFREFELSEREFTlbConstructor {
    override fun toString(): String = "IFREFELSEREF"
}

private object IFREFELSEREFTlbConstructor : TlbConstructor<IFREFELSEREF>(
    schema = "asm_ifrefelseref#e30f = IFREFELSEREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREFELSEREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFREFELSEREF = IFREFELSEREF
}