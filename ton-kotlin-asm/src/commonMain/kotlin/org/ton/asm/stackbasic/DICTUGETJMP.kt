package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETJMP : AsmInstruction, TlbConstructorProvider<DICTUGETJMP> by DICTUGETJMPTlbConstructor {
    override fun toString(): String = "DICTUGETJMP"
}

private object DICTUGETJMPTlbConstructor : TlbConstructor<DICTUGETJMP>(
    schema = "asm_dictugetjmp#f4a1 = DICTUGETJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETJMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETJMP = DICTUGETJMP
}