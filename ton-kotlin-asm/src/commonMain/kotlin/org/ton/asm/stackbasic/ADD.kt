package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ADD : AsmInstruction, TlbConstructorProvider<ADD> by ADDTlbConstructor {
    override fun toString(): String = "ADD"
}

private object ADDTlbConstructor : TlbConstructor<ADD>(
    schema = "asm_add#a0 = ADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): ADD = ADD
}