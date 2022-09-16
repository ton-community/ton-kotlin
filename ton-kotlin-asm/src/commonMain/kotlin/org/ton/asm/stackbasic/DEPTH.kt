package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object DEPTH : AsmInstruction, TlbConstructorProvider<DEPTH> by DEPTHTlbConstructor {
    override fun toString(): String = "DEPTH"
}

private object DEPTHTlbConstructor : TlbConstructor<DEPTH>(
    schema = "asm_depth#68 = DEPTH;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DEPTH) {
    }

    override fun loadTlb(cellSlice: CellSlice): DEPTH = DEPTH
}