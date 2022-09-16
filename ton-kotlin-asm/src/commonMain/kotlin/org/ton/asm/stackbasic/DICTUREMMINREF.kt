package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREMMINREF : AsmInstruction, TlbConstructorProvider<DICTUREMMINREF> by DICTUREMMINREFTlbConstructor {
    override fun toString(): String = "DICTUREMMINREF"
}

private object DICTUREMMINREFTlbConstructor : TlbConstructor<DICTUREMMINREF>(
    schema = "asm_dicturemminref#f497 = DICTUREMMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREMMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREMMINREF = DICTUREMMINREF
}