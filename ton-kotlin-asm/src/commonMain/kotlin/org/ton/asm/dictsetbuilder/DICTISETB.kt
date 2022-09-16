package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETB : AsmInstruction, TlbConstructorProvider<DICTISETB> by DICTISETBTlbConstructor {
    override fun toString(): String = "DICTISETB"
}

private object DICTISETBTlbConstructor : TlbConstructor<DICTISETB>(
    schema = "asm_dictisetb#f442 = DICTISETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETB = DICTISETB
}
