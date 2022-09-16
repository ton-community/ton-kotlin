package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUB : AsmInstruction, TlbConstructorProvider<SUB> by SUBTlbConstructor {
    override fun toString(): String = "SUB"
}

private object SUBTlbConstructor : TlbConstructor<SUB>(
    schema = "asm_sub#a1 = SUB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUB) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUB = SUB
}