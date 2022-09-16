package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTSETGETREF : AsmInstruction, TlbConstructorProvider<DICTSETGETREF> by DICTSETGETREFTlbConstructor {
    override fun toString(): String = "DICTSETGETREF"
}

private object DICTSETGETREFTlbConstructor : TlbConstructor<DICTSETGETREF>(
    schema = "asm_dictsetgetref#f41b = DICTSETGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTSETGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTSETGETREF = DICTSETGETREF
}