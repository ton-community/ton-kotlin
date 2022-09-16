package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETCONTVARARGS : AsmInstruction, TlbConstructorProvider<SETCONTVARARGS> by SETCONTVARARGSTlbConstructor {
    override fun toString(): String = "SETCONTVARARGS"
}

private object SETCONTVARARGSTlbConstructor : TlbConstructor<SETCONTVARARGS>(
    schema = "asm_setcontvarargs#ed11 = SETCONTVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONTVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONTVARARGS = SETCONTVARARGS
}
