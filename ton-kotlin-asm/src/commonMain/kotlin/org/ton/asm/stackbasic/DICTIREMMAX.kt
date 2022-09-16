package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIREMMAX : AsmInstruction, TlbConstructorProvider<DICTIREMMAX> by DICTIREMMAXTlbConstructor {
    override fun toString(): String = "DICTIREMMAX"
}

private object DICTIREMMAXTlbConstructor : TlbConstructor<DICTIREMMAX>(
    schema = "asm_dictiremmax#f49c = DICTIREMMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIREMMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIREMMAX = DICTIREMMAX
}