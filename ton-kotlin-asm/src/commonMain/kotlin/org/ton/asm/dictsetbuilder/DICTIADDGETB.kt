package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIADDGETB : AsmInstruction, TlbConstructorProvider<DICTIADDGETB> by DICTIADDGETBTlbConstructor {
    override fun toString(): String = "DICTIADDGETB"
}

private object DICTIADDGETBTlbConstructor : TlbConstructor<DICTIADDGETB>(
    schema = "asm_dictiaddgetb#f456 = DICTIADDGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIADDGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIADDGETB = DICTIADDGETB
}
