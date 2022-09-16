package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CTOS : AsmInstruction, TlbConstructorProvider<CTOS> by CTOSTlbConstructor {
    override fun toString(): String = "CTOS"
}

private object CTOSTlbConstructor : TlbConstructor<CTOS>(
    schema = "asm_ctos#d0 = CTOS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CTOS) {
    }

    override fun loadTlb(cellSlice: CellSlice): CTOS = CTOS
}
