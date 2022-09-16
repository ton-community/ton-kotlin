package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object GREATER : AsmInstruction, TlbConstructorProvider<GREATER> by GREATERTlbConstructor {
    override fun toString(): String = "GREATER"
}

private object GREATERTlbConstructor : TlbConstructor<GREATER>(
    schema = "asm_greater#bc = GREATER;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GREATER) {
    }

    override fun loadTlb(cellSlice: CellSlice): GREATER = GREATER
}