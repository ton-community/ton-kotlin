package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNEG : Instruction, TlbConstructorProvider<ISNEG> by ISNEGTlbConstructor {
    override fun toString(): String = "ISNEG"
}

private object ISNEGTlbConstructor : TlbConstructor<ISNEG>(
    schema = "asm_isneg#c100 = ISNEG;",
    type = ISNEG::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNEG) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNEG = ISNEG
}