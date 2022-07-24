package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object POW2 : Instruction, TlbConstructorProvider<POW2> by POW2TlbConstructor {
    override fun toString(): String = "POW2"
}

private object POW2TlbConstructor : TlbConstructor<POW2>(
    schema = "asm_pow2#ae = POW2;",
    type = POW2::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POW2) {
    }

    override fun loadTlb(cellSlice: CellSlice): POW2 = POW2
}