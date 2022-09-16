package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object FITSX : AsmInstruction, TlbConstructorProvider<FITSX> by FITSXTlbConstructor {
    override fun toString(): String = "FITSX"
}

private object FITSXTlbConstructor : TlbConstructor<FITSX>(
    schema = "asm_fitsx#b600 = FITSX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FITSX) {
    }

    override fun loadTlb(cellSlice: CellSlice): FITSX = FITSX
}