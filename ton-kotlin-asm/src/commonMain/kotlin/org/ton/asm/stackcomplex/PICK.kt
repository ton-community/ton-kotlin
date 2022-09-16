package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PICK : AsmInstruction, TlbConstructorProvider<PICK> by PICKTlbConstructor {
    override fun toString(): String = "PICK"
}

private object PICKTlbConstructor : TlbConstructor<PICK>(
    schema = "asm_pick#60 = PICK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PICK) {
    }

    override fun loadTlb(cellSlice: CellSlice): PICK = PICK
}
