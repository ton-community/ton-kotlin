package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADDREF : AsmInstruction, TlbConstructorProvider<DICTUADDREF> by DICTUADDREFTlbConstructor {
    override fun toString(): String = "DICTUADDREF"
}

private object DICTUADDREFTlbConstructor : TlbConstructor<DICTUADDREF>(
    schema = "asm_dictuaddref#f437 = DICTUADDREF;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADDREF) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADDREF = DICTUADDREF
}
