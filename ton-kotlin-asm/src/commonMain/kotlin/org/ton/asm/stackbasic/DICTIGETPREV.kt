package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETPREV : AsmInstruction, TlbConstructorProvider<DICTIGETPREV> by DICTIGETPREVTlbConstructor {
    override fun toString(): String = "DICTIGETPREV"
}

private object DICTIGETPREVTlbConstructor : TlbConstructor<DICTIGETPREV>(
    schema = "asm_dictigetprev#f47a = DICTIGETPREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETPREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETPREV = DICTIGETPREV
}