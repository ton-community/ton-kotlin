package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RETURNVARARGS : AsmInstruction, TlbConstructorProvider<RETURNVARARGS> by RETURNVARARGSTlbConstructor {
    override fun toString(): String = "RETURNVARARGS"
}

private object RETURNVARARGSTlbConstructor : TlbConstructor<RETURNVARARGS>(
    schema = "asm_returnvarargs#ed10 = RETURNVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETURNVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): RETURNVARARGS = RETURNVARARGS
}
