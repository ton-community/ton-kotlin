package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADDGET : AsmInstruction, TlbConstructorProvider<DICTUADDGET> by DICTUADDGETTlbConstructor {
    override fun toString(): String = "DICTUADDGET"
}

private object DICTUADDGETTlbConstructor : TlbConstructor<DICTUADDGET>(
    schema = "asm_dictuaddget#f43e = DICTUADDGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADDGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADDGET = DICTUADDGET
}