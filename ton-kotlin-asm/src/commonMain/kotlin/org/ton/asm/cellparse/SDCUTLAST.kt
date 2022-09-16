package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDCUTLAST : AsmInstruction, TlbConstructorProvider<SDCUTLAST> by SDCUTLASTTlbConstructor {
    override fun toString(): String = "SDCUTLAST"
}

private object SDCUTLASTTlbConstructor : TlbConstructor<SDCUTLAST>(
    schema = "asm_sdcutlast#d722 = SDCUTLAST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDCUTLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDCUTLAST = SDCUTLAST
}
