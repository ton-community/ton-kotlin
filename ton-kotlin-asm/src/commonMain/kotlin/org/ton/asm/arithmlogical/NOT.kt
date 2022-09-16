package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NOT : AsmInstruction, TlbConstructorProvider<NOT> by NOTTlbConstructor {
    override fun toString(): String = "NOT"
}

private object NOTTlbConstructor : TlbConstructor<NOT>(
    schema = "asm_not#b3 = NOT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOT) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOT = NOT
}
