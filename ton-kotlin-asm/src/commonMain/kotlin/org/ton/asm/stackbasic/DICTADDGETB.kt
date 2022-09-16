package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTADDGETB : AsmInstruction, TlbConstructorProvider<DICTADDGETB> by DICTADDGETBTlbConstructor {
    override fun toString(): String = "DICTADDGETB"
}

private object DICTADDGETBTlbConstructor : TlbConstructor<DICTADDGETB>(
    schema = "asm_dictaddgetb#f455 = DICTADDGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTADDGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTADDGETB = DICTADDGETB
}