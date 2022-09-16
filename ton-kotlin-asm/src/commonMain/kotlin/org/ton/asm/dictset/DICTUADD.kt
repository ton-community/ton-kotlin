package org.ton.asm.dictset

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUADD : AsmInstruction, TlbConstructorProvider<DICTUADD> by DICTUADDTlbConstructor {
    override fun toString(): String = "DICTUADD"
}

private object DICTUADDTlbConstructor : TlbConstructor<DICTUADD>(
    schema = "asm_dictuadd#f436 = DICTUADD;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUADD) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUADD = DICTUADD
}
