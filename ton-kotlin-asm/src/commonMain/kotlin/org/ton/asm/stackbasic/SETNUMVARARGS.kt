package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SETNUMVARARGS : AsmInstruction, TlbConstructorProvider<SETNUMVARARGS> by SETNUMVARARGSTlbConstructor {
    override fun toString(): String = "SETNUMVARARGS"
}

private object SETNUMVARARGSTlbConstructor : TlbConstructor<SETNUMVARARGS>(
    schema = "asm_setnumvarargs#ed12 = SETNUMVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETNUMVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SETNUMVARARGS = SETNUMVARARGS
}