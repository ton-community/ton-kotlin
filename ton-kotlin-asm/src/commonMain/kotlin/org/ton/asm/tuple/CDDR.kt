package org.ton.asm.tuple

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDDR : AsmInstruction, TlbConstructorProvider<CDDR> by CDDRTlbConstructor {
    override fun toString(): String = "CDDR"
}

private object CDDRTlbConstructor : TlbConstructor<CDDR>(
    schema = "asm_cddr#6fb5 = CDDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDDR = CDDR
}
