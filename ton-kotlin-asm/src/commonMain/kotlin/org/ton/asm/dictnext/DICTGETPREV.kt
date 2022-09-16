package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETPREV : AsmInstruction, TlbConstructorProvider<DICTGETPREV> by DICTGETPREVTlbConstructor {
    override fun toString(): String = "DICTGETPREV"
}

private object DICTGETPREVTlbConstructor : TlbConstructor<DICTGETPREV>(
    schema = "asm_dictgetprev#f476 = DICTGETPREV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETPREV) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETPREV = DICTGETPREV
}
