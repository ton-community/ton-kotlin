package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREMMIN : AsmInstruction, TlbConstructorProvider<DICTIREMMIN> by DICTIREMMINTlbConstructor {
    override fun toString(): String = "DICTIREMMIN"
}

private object DICTIREMMINTlbConstructor : TlbConstructor<DICTIREMMIN>(
    schema = "asm_dictiremmin#f494 = DICTIREMMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREMMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREMMIN = DICTIREMMIN
}
