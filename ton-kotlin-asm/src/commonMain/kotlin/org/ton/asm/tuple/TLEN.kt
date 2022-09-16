package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object TLEN : AsmInstruction, TlbConstructorProvider<TLEN> by TLENTlbConstructor {
    override fun toString(): String = "TLEN"
}

private object TLENTlbConstructor : TlbConstructor<TLEN>(
    schema = "asm_tlen#6f88 = TLEN;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TLEN) {
    }

    override fun loadTlb(cellSlice: CellSlice): TLEN = TLEN
}
