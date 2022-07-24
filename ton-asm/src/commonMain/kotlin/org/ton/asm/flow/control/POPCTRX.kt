package org.ton.asm.flow.control

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object POPCTRX : Instruction, TlbConstructorProvider<POPCTRX> by POPCTRXTlbConstructor {
    override fun toString(): String = "POPCTRX"
}

private object POPCTRXTlbConstructor : TlbConstructor<POPCTRX>(
    schema = "asm_popctrx#ede1 = POPCTRX;",
    type = POPCTRX::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPCTRX) {
    }

    override fun loadTlb(cellSlice: CellSlice): POPCTRX = POPCTRX
}