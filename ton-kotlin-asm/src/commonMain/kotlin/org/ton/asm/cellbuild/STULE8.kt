package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STULE8 : AsmInstruction, TlbConstructorProvider<STULE8> by STULE8TlbConstructor {
    override fun toString(): String = "STULE8"
}

private object STULE8TlbConstructor : TlbConstructor<STULE8>(
    schema = "asm_stule8#cf2b = STULE8;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STULE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): STULE8 = STULE8
}
