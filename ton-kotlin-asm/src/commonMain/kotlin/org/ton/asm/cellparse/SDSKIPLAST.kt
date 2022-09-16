package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDSKIPLAST : AsmInstruction, TlbConstructorProvider<SDSKIPLAST> by SDSKIPLASTTlbConstructor {
    override fun toString(): String = "SDSKIPLAST"
}

private object SDSKIPLASTTlbConstructor : TlbConstructor<SDSKIPLAST>(
    schema = "asm_sdskiplast#d723 = SDSKIPLAST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDSKIPLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDSKIPLAST = SDSKIPLAST
}
