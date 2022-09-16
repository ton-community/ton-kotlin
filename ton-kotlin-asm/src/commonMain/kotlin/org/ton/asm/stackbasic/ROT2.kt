package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ROT2 : AsmInstruction, TlbConstructorProvider<ROT2> by ROT2TlbConstructor {
    override fun toString(): String = "ROT2"
}

private object ROT2TlbConstructor : TlbConstructor<ROT2>(
    schema = "asm_rot2#5513 = ROT2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROT2) {
    }

    override fun loadTlb(cellSlice: CellSlice): ROT2 = ROT2
}