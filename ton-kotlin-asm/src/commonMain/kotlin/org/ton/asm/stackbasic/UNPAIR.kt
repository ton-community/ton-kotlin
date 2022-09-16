package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object UNPAIR : AsmInstruction, TlbConstructorProvider<UNPAIR> by UNPAIRTlbConstructor {
    override fun toString(): String = "UNPAIR"
}

private object UNPAIRTlbConstructor : TlbConstructor<UNPAIR>(
    schema = "asm_unpair#6f22 = UNPAIR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: UNPAIR) {
    }

    override fun loadTlb(cellSlice: CellSlice): UNPAIR = UNPAIR
}