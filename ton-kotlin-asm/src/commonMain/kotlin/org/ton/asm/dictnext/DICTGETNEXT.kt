package org.ton.asm.dictnext

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTGETNEXT : AsmInstruction, TlbConstructorProvider<DICTGETNEXT> by DICTGETNEXTTlbConstructor {
    override fun toString(): String = "DICTGETNEXT"
}

private object DICTGETNEXTTlbConstructor : TlbConstructor<DICTGETNEXT>(
    schema = "asm_dictgetnext#f474 = DICTGETNEXT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTGETNEXT) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTGETNEXT = DICTGETNEXT
}
