package org.ton.asm.apprnd

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RAND : AsmInstruction, TlbConstructorProvider<RAND> by RANDTlbConstructor {
    override fun toString(): String = "RAND"
}

private object RANDTlbConstructor : TlbConstructor<RAND>(
    schema = "asm_rand#f811 = RAND;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RAND) {
    }

    override fun loadTlb(cellSlice: CellSlice): RAND = RAND
}
