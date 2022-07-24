package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFNOTREF : Instruction, TlbConstructorProvider<IFNOTREF> by IFNOTREFTlbConstructor {
    override fun toString(): String = "IFNOTREF"
}

private object IFNOTREFTlbConstructor : TlbConstructor<IFNOTREF>(
    schema = "asm_ifnotref#e301 = IFNOTREF;",
    type = IFNOTREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFNOTREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFNOTREF = IFNOTREF
}