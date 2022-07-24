package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object GREATER : Instruction, TlbConstructorProvider<GREATER> by GREATERTLbConstructor {
    override fun toString(): String = "GREATER"
}

private object GREATERTLbConstructor : TlbConstructor<GREATER>(
    schema = "asm_greater#bc = GREATER;",
    type = GREATER::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GREATER) {
    }

    override fun loadTlb(cellSlice: CellSlice): GREATER = GREATER
}