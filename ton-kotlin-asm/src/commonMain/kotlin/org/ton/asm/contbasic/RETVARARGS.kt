package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RETVARARGS : AsmInstruction, TlbConstructorProvider<RETVARARGS> by RETVARARGSTlbConstructor {
    override fun toString(): String = "RETVARARGS"
}

private object RETVARARGSTlbConstructor : TlbConstructor<RETVARARGS>(
    schema = "asm_retvarargs#db39 = RETVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): RETVARARGS = RETVARARGS
}
