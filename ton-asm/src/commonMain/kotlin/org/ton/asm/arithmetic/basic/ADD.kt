package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ADD : Instruction, TlbConstructorProvider<ADD> by ADDTlbConstructor {
    override fun toString(): String = "ADD"
}

private object ADDTlbConstructor : TlbConstructor<ADD>(
    schema = "asm_add#a0 = ADD;",
    type = ADD::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): ADD = ADD
}