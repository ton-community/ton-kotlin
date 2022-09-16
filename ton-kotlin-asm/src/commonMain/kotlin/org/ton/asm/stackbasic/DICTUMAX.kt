package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTUMAX : AsmInstruction, TlbConstructorProvider<DICTUMAX> by DICTUMAXTlbConstructor {
    override fun toString(): String = "DICTUMAX"
}

private object DICTUMAXTlbConstructor : TlbConstructor<DICTUMAX>(
    schema = "asm_dictumax#f48e = DICTUMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTUMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTUMAX = DICTUMAX
}