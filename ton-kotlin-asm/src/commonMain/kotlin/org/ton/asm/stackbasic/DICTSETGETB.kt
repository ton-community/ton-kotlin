package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETGETB : AsmInstruction, TlbConstructorProvider<DICTSETGETB> by DICTSETGETBTlbConstructor {
    override fun toString(): String = "DICTSETGETB"
}

private object DICTSETGETBTlbConstructor : TlbConstructor<DICTSETGETB>(
    schema = "asm_dictsetgetb#f445 = DICTSETGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETGETB = DICTSETGETB
}