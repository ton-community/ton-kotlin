package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object WHILE : AsmInstruction, TlbConstructorProvider<WHILE> by WHILETlbConstructor {
    override fun toString(): String = "WHILE"
}

private object WHILETlbConstructor : TlbConstructor<WHILE>(
    schema = "asm_while#e8 = WHILE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: WHILE) {
    }

    override fun loadTlb(cellSlice: CellSlice): WHILE = WHILE
}