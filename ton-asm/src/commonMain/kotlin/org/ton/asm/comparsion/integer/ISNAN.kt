package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNAN : Instruction, TlbConstructorProvider<ISNAN> by ISNANTlbConstructor {
    override fun toString(): String = "ISNAN"
}

private object ISNANTlbConstructor : TlbConstructor<ISNAN>(
    schema = "asm_isnan#c4 = ISNAN;",
    type = ISNAN::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNAN) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNAN = ISNAN
}