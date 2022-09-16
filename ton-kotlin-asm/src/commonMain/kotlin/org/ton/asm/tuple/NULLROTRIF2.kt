package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLROTRIF2 : AsmInstruction, TlbConstructorProvider<NULLROTRIF2> by NULLROTRIF2TlbConstructor {
    override fun toString(): String = "NULLROTRIF2"
}

private object NULLROTRIF2TlbConstructor : TlbConstructor<NULLROTRIF2>(
    schema = "asm_nullrotrif2#6fa6 = NULLROTRIF2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLROTRIF2) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLROTRIF2 = NULLROTRIF2
}
