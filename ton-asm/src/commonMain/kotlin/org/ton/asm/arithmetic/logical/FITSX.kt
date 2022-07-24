package org.ton.asm.arithmetic.logical

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object FITSX : Instruction, TlbConstructorProvider<FITSX> by FITSXTlbConstructor {
    override fun toString(): String = "FITSX"
}

private object FITSXTlbConstructor : TlbConstructor<FITSX>(
    schema = "asm_fitsx#b600 = FITSX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): FITSX = FITSX
}