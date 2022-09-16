package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object ROT : AsmInstruction, TlbConstructorProvider<ROT> by ROTTlbConstructor {
    override fun toString(): String = "ROT"
}

private object ROTTlbConstructor : TlbConstructor<ROT>(
    schema = "asm_rot#58 = ROT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ROT) {
    }

    override fun loadTlb(cellSlice: CellSlice): ROT = ROT
}
