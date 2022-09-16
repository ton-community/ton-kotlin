package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NULLROTRIF : AsmInstruction, TlbConstructorProvider<NULLROTRIF> by NULLROTRIFTlbConstructor {
    override fun toString(): String = "NULLROTRIF"
}

private object NULLROTRIFTlbConstructor : TlbConstructor<NULLROTRIF>(
    schema = "asm_nullrotrif#6fa2 = NULLROTRIF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NULLROTRIF) {
    }

    override fun loadTlb(cellSlice: CellSlice): NULLROTRIF = NULLROTRIF
}