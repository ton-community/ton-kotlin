package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETPREV : AsmInstruction, TlbConstructorProvider<DICTUGETPREV> by DICTUGETPREVTlbConstructor {
    override fun toString(): String = "DICTUGETPREV"
}

private object DICTUGETPREVTlbConstructor : TlbConstructor<DICTUGETPREV>(
    schema = "asm_dictugetprev#f47e = DICTUGETPREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETPREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETPREV = DICTUGETPREV
}