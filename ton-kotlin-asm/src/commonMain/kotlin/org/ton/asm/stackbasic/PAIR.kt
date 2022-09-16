package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object PAIR : AsmInstruction, TlbConstructorProvider<PAIR> by PAIRTlbConstructor {
    override fun toString(): String = "PAIR"
}

private object PAIRTlbConstructor : TlbConstructor<PAIR>(
    schema = "asm_pair#6f02 = PAIR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PAIR) {
    }

    override fun loadTlb(cellSlice: CellSlice): PAIR = PAIR
}