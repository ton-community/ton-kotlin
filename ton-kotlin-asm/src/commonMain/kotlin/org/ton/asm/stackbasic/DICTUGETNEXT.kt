package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUGETNEXT : AsmInstruction, TlbConstructorProvider<DICTUGETNEXT> by DICTUGETNEXTTlbConstructor {
    override fun toString(): String = "DICTUGETNEXT"
}

private object DICTUGETNEXTTlbConstructor : TlbConstructor<DICTUGETNEXT>(
    schema = "asm_dictugetnext#f47c = DICTUGETNEXT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUGETNEXT) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUGETNEXT = DICTUGETNEXT
}