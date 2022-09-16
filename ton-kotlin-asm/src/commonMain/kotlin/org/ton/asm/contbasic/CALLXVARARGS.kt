package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CALLXVARARGS : AsmInstruction, TlbConstructorProvider<CALLXVARARGS> by CALLXVARARGSTlbConstructor {
    override fun toString(): String = "CALLXVARARGS"
}

private object CALLXVARARGSTlbConstructor : TlbConstructor<CALLXVARARGS>(
    schema = "asm_callxvarargs#db38 = CALLXVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CALLXVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): CALLXVARARGS = CALLXVARARGS
}
