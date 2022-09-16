package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object OR : AsmInstruction, TlbConstructorProvider<OR> by ORTlbConstructor {
    override fun toString(): String = "OR"
}

private object ORTlbConstructor : TlbConstructor<OR>(
    schema = "asm_or#b1 = OR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: OR) {
    }

    override fun loadTlb(cellSlice: CellSlice): OR = OR
}
