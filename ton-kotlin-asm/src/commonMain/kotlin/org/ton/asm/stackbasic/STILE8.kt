package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STILE8 : AsmInstruction, TlbConstructorProvider<STILE8> by STILE8TlbConstructor {
    override fun toString(): String = "STILE8"
}

private object STILE8TlbConstructor : TlbConstructor<STILE8>(
    schema = "asm_stile8#cf2a = STILE8;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STILE8) {
    }

    override fun loadTlb(cellSlice: CellSlice): STILE8 = STILE8
}