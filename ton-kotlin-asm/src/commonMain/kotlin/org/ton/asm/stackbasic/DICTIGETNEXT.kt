package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIGETNEXT : AsmInstruction, TlbConstructorProvider<DICTIGETNEXT> by DICTIGETNEXTTlbConstructor {
    override fun toString(): String = "DICTIGETNEXT"
}

private object DICTIGETNEXTTlbConstructor : TlbConstructor<DICTIGETNEXT>(
    schema = "asm_dictigetnext#f478 = DICTIGETNEXT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIGETNEXT) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIGETNEXT = DICTIGETNEXT
}