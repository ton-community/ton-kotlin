package org.ton.asm.comparsion.integer

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SGN : Instruction, TlbConstructorProvider<SGN> by SGNTlbConstructor {
    override fun toString(): String = "SGN"
}

private object SGNTlbConstructor : TlbConstructor<SGN>(
    schema = "asm_sgn#b8 = SGN;",
    type = SGN::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SGN) {
    }

    override fun loadTlb(cellSlice: CellSlice): SGN = SGN
}