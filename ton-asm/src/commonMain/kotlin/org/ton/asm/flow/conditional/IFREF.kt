package org.ton.asm.flow.conditional

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object IFREF : Instruction, TlbConstructorProvider<IFREF> by IFREFTlbConstructor {
    override fun toString(): String = "IFREF"
}

private object IFREFTlbConstructor : TlbConstructor<IFREF>(
    schema = "asm_ifref#e300 = IFREF;",
    type = IFREF::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: IFREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): IFREF = IFREF
}