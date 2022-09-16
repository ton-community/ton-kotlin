package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUMIN : AsmInstruction, TlbConstructorProvider<DICTUMIN> by DICTUMINTlbConstructor {
    override fun toString(): String = "DICTUMIN"
}

private object DICTUMINTlbConstructor : TlbConstructor<DICTUMIN>(
    schema = "asm_dictumin#f486 = DICTUMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUMIN = DICTUMIN
}