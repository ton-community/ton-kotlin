package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CMP : AsmInstruction, TlbConstructorProvider<CMP> by CMPTlbConstructor {
    override fun toString(): String = "CMP"
}

private object CMPTlbConstructor : TlbConstructor<CMP>(
    schema = "asm_cmp#bf = CMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): CMP = CMP
}