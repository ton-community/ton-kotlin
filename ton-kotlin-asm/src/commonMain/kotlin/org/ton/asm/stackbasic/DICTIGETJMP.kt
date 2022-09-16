package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETJMP : AsmInstruction, TlbConstructorProvider<DICTIGETJMP> by DICTIGETJMPTlbConstructor {
    override fun toString(): String = "DICTIGETJMP"
}

private object DICTIGETJMPTlbConstructor : TlbConstructor<DICTIGETJMP>(
    schema = "asm_dictigetjmp#f4a0 = DICTIGETJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETJMP = DICTIGETJMP
}