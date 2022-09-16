package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object NOW : AsmInstruction, TlbConstructorProvider<NOW> by NOWTlbConstructor {
    override fun toString(): String = "NOW"
}

private object NOWTlbConstructor : TlbConstructor<NOW>(
    schema = "asm_now#f823 = NOW;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: NOW) {
    }

    override fun loadTlb(cellSlice: CellSlice): NOW = NOW
}