package org.ton.asm.arithmlogical

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MIN : AsmInstruction, TlbConstructorProvider<MIN> by MINTlbConstructor {
    override fun toString(): String = "MIN"
}

private object MINTlbConstructor : TlbConstructor<MIN>(
    schema = "asm_min#b608 = MIN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MIN) {
    }

    override fun loadTlb(cellSlice: CellSlice): MIN = MIN
}
