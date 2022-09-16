package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object POPCTRX : AsmInstruction, TlbConstructorProvider<POPCTRX> by POPCTRXTlbConstructor {
    override fun toString(): String = "POPCTRX"
}

private object POPCTRXTlbConstructor : TlbConstructor<POPCTRX>(
    schema = "asm_popctrx#ede1 = POPCTRX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POPCTRX) {
    }

    override fun loadTlb(cellSlice: CellSlice): POPCTRX = POPCTRX
}