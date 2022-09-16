package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STULE4 : AsmInstruction, TlbConstructorProvider<STULE4> by STULE4TlbConstructor {
    override fun toString(): String = "STULE4"
}

private object STULE4TlbConstructor : TlbConstructor<STULE4>(
    schema = "asm_stule4#cf29 = STULE4;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STULE4) {
    }

    override fun loadTlb(cellSlice: CellSlice): STULE4 = STULE4
}