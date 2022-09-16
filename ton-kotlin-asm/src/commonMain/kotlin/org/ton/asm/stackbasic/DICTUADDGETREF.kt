package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADDGETREF : AsmInstruction, TlbConstructorProvider<DICTUADDGETREF> by DICTUADDGETREFTlbConstructor {
    override fun toString(): String = "DICTUADDGETREF"
}

private object DICTUADDGETREFTlbConstructor : TlbConstructor<DICTUADDGETREF>(
    schema = "asm_dictuaddgetref#f43f = DICTUADDGETREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADDGETREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADDGETREF = DICTUADDGETREF
}