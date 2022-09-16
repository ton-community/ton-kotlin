package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object REVX : AsmInstruction, TlbConstructorProvider<REVX> by REVXTlbConstructor {
    override fun toString(): String = "REVX"
}

private object REVXTlbConstructor : TlbConstructor<REVX>(
    schema = "asm_revx#64 = REVX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: REVX) {
    }

    override fun loadTlb(cellSlice: CellSlice): REVX = REVX
}
