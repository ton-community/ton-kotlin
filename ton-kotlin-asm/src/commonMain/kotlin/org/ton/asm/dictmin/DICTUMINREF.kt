package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUMINREF : AsmInstruction, TlbConstructorProvider<DICTUMINREF> by DICTUMINREFTlbConstructor {
    override fun toString(): String = "DICTUMINREF"
}

private object DICTUMINREFTlbConstructor : TlbConstructor<DICTUMINREF>(
    schema = "asm_dictuminref#f487 = DICTUMINREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUMINREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUMINREF = DICTUMINREF
}
