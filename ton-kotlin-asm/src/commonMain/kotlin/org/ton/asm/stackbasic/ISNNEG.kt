package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ISNNEG : AsmInstruction, TlbConstructorProvider<ISNNEG> by ISNNEGTlbConstructor {
    override fun toString(): String = "ISNNEG"
}

private object ISNNEGTlbConstructor : TlbConstructor<ISNNEG>(
    schema = "asm_isnneg#c2ff = ISNNEG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ISNNEG) {
    }

    override fun loadTlb(cellSlice: CellSlice): ISNNEG = ISNNEG
}