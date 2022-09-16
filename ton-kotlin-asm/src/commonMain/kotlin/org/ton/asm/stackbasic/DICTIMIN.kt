package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DICTIMIN : AsmInstruction, TlbConstructorProvider<DICTIMIN> by DICTIMINTlbConstructor {
    override fun toString(): String = "DICTIMIN"
}

private object DICTIMINTlbConstructor : TlbConstructor<DICTIMIN>(
    schema = "asm_dictimin#f484 = DICTIMIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTIMIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): DICTIMIN = DICTIMIN
}