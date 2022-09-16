package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTISETGETB : AsmInstruction, TlbConstructorProvider<DICTISETGETB> by DICTISETGETBTlbConstructor {
    override fun toString(): String = "DICTISETGETB"
}

private object DICTISETGETBTlbConstructor : TlbConstructor<DICTISETGETB>(
    schema = "asm_dictisetgetb#f446 = DICTISETGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTISETGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTISETGETB = DICTISETGETB
}
