package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNEG : AsmInstruction, TlbConstructorProvider<ISNEG> by ISNEGTlbConstructor {
    override fun toString(): String = "ISNEG"
}

private object ISNEGTlbConstructor : TlbConstructor<ISNEG>(
    schema = "asm_isneg#c100 = ISNEG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNEG) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNEG = ISNEG
}
