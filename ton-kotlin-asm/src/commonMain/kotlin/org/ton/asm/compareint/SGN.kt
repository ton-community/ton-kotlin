package org.ton.asm.compareint

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SGN : AsmInstruction, TlbConstructorProvider<SGN> by SGNTlbConstructor {
    override fun toString(): String = "SGN"
}

private object SGNTlbConstructor : TlbConstructor<SGN>(
    schema = "asm_sgn#b8 = SGN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SGN) {
    }

    override fun loadTlb(cellSlice: CellSlice): SGN = SGN
}
