package org.ton.asm.dictmin

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUREMMAX : AsmInstruction, TlbConstructorProvider<DICTUREMMAX> by DICTUREMMAXTlbConstructor {
    override fun toString(): String = "DICTUREMMAX"
}

private object DICTUREMMAXTlbConstructor : TlbConstructor<DICTUREMMAX>(
    schema = "asm_dicturemmax#f49e = DICTUREMMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUREMMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUREMMAX = DICTUREMMAX
}
