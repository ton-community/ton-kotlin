package org.ton.asm.contcreate

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object BLESSVARARGS : AsmInstruction, TlbConstructorProvider<BLESSVARARGS> by BLESSVARARGSTlbConstructor {
    override fun toString(): String = "BLESSVARARGS"
}

private object BLESSVARARGSTlbConstructor : TlbConstructor<BLESSVARARGS>(
    schema = "asm_blessvarargs#ed1f = BLESSVARARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLESSVARARGS) {
    }

    override fun loadTlb(cellSlice: CellSlice): BLESSVARARGS = BLESSVARARGS
}
