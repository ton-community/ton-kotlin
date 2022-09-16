package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DIVC : AsmInstruction, TlbConstructorProvider<DIVC> by DIVCTlbConstructor {
    override fun toString(): String = "DIVC"
}

private object DIVCTlbConstructor : TlbConstructor<DIVC>(
    schema = "asm_divc#a906 = DIVC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DIVC) {
    }

    override fun loadTlb(cellSlice: CellSlice): DIVC = DIVC
}