package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADDGETB : AsmInstruction, TlbConstructorProvider<DICTUADDGETB> by DICTUADDGETBTlbConstructor {
    override fun toString(): String = "DICTUADDGETB"
}

private object DICTUADDGETBTlbConstructor : TlbConstructor<DICTUADDGETB>(
    schema = "asm_dictuaddgetb#f457 = DICTUADDGETB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADDGETB) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADDGETB = DICTUADDGETB
}