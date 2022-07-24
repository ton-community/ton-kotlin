package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CMP : Instruction, TlbConstructorProvider<CMP> by CMPTlbConstructor {
    override fun toString(): String = "CMP"
}

private object CMPTlbConstructor : TlbConstructor<CMP>(
    schema = "asm_cmp#bf = CMP;",
    type = CMP::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): CMP = CMP
}