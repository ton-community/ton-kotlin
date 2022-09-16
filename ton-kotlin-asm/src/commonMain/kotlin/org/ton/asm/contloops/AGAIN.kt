package org.ton.asm.contloops

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object AGAIN : AsmInstruction, TlbConstructorProvider<AGAIN> by AGAINTlbConstructor {
    override fun toString(): String = "AGAIN"
}

private object AGAINTlbConstructor : TlbConstructor<AGAIN>(
    schema = "asm_again#ea = AGAIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AGAIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): AGAIN = AGAIN
}
