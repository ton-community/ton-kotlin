package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DROPX : AsmInstruction, TlbConstructorProvider<DROPX> by DROPXTlbConstructor {
    override fun toString(): String = "DROPX"
}

private object DROPXTlbConstructor : TlbConstructor<DROPX>(
    schema = "asm_dropx#65 = DROPX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DROPX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DROPX = DROPX
}
