package org.ton.asm.dictsetbuilder

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUSETGETB : AsmInstruction, TlbConstructorProvider<DICTUSETGETB> by DICTUSETGETBTlbConstructor {
    override fun toString(): String = "DICTUSETGETB"
}

private object DICTUSETGETBTlbConstructor : TlbConstructor<DICTUSETGETB>(
    schema = "asm_dictusetgetb#f447 = DICTUSETGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUSETGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUSETGETB = DICTUSETGETB
}
