package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object STONES : AsmInstruction, TlbConstructorProvider<STONES> by STONESTlbConstructor {
    override fun toString(): String = "STONES"
}

private object STONESTlbConstructor : TlbConstructor<STONES>(
    schema = "asm_stones#cf41 = STONES;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STONES) {
    }

    override fun loadTlb(cellSlice: CellSlice): STONES = STONES
}