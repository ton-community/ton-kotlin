package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ROTREV : AsmInstruction, TlbConstructorProvider<ROTREV> by ROTREVTlbConstructor {
    override fun toString(): String = "ROTREV"
}

private object ROTREVTlbConstructor : TlbConstructor<ROTREV>(
    schema = "asm_rotrev#59 = ROTREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROTREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): ROTREV = ROTREV
}
