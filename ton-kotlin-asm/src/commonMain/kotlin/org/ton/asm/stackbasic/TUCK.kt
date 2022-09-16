package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TUCK : AsmInstruction, TlbConstructorProvider<TUCK> by TUCKTlbConstructor {
    override fun toString(): String = "TUCK"
}

private object TUCKTlbConstructor : TlbConstructor<TUCK>(
    schema = "asm_tuck#66 = TUCK;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TUCK) {
    }

    override fun loadTlb(cellSlice: CellSlice): TUCK = TUCK
}