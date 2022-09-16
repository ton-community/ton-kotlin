package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CADDR : AsmInstruction, TlbConstructorProvider<CADDR> by CADDRTlbConstructor {
    override fun toString(): String = "CADDR"
}

private object CADDRTlbConstructor : TlbConstructor<CADDR>(
    schema = "asm_caddr#6fd4 = CADDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CADDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): CADDR = CADDR
}
