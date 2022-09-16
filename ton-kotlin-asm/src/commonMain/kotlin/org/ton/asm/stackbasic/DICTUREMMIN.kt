package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREMMIN : AsmInstruction, TlbConstructorProvider<DICTUREMMIN> by DICTUREMMINTlbConstructor {
    override fun toString(): String = "DICTUREMMIN"
}

private object DICTUREMMINTlbConstructor : TlbConstructor<DICTUREMMIN>(
    schema = "asm_dicturemmin#f496 = DICTUREMMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREMMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREMMIN = DICTUREMMIN
}