package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCUTLAST : AsmInstruction, TlbConstructorProvider<SCUTLAST> by SCUTLASTTlbConstructor {
    override fun toString(): String = "SCUTLAST"
}

private object SCUTLASTTlbConstructor : TlbConstructor<SCUTLAST>(
    schema = "asm_scutlast#d732 = SCUTLAST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCUTLAST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCUTLAST = SCUTLAST
}
