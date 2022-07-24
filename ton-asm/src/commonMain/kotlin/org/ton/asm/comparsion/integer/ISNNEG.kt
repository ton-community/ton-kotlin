package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNNEG : Instruction, TlbConstructorProvider<ISNNEG> by ISNNEGTLbConstructor {
    override fun toString(): String = "ISNNEG"
}

private object ISNNEGTLbConstructor : TlbConstructor<ISNNEG>(
    schema = "asm_isnneg#c2ff = ISNNEG;",
    type = ISNNEG::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNNEG) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNNEG = ISNNEG
}