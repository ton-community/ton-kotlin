package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object MINMAX : Instruction, TlbConstructorProvider<MINMAX> by MINMAXTlbConstructor {
    override fun toString(): String = "MINMAX"
}

private object MINMAXTlbConstructor : TlbConstructor<MINMAX>(
    schema = "asm_minmax#b60a = MINMAX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MINMAX) {
    }

    override fun loadTlb(cellSlice: CellSlice): MINMAX = MINMAX
}