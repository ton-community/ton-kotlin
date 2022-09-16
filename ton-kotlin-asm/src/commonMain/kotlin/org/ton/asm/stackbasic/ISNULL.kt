package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNULL : AsmInstruction, TlbConstructorProvider<ISNULL> by ISNULLTlbConstructor {
    override fun toString(): String = "ISNULL"
}

private object ISNULLTlbConstructor : TlbConstructor<ISNULL>(
    schema = "asm_isnull#6e = ISNULL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNULL) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNULL = ISNULL
}