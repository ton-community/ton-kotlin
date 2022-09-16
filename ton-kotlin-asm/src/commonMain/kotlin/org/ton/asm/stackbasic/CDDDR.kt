package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object CDDDR : AsmInstruction, TlbConstructorProvider<CDDDR> by CDDDRTlbConstructor {
    override fun toString(): String = "CDDDR"
}

private object CDDDRTlbConstructor : TlbConstructor<CDDDR>(
    schema = "asm_cdddr#6fd5 = CDDDR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: CDDDR) {
    }

    override fun loadTlb(cellSlice: CellSlice): CDDDR = CDDDR
}