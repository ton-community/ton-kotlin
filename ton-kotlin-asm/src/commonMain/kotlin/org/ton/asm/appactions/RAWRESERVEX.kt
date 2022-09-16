package org.ton.asm.appactions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RAWRESERVEX : AsmInstruction, TlbConstructorProvider<RAWRESERVEX> by RAWRESERVEXTlbConstructor {
    override fun toString(): String = "RAWRESERVEX"
}

private object RAWRESERVEXTlbConstructor : TlbConstructor<RAWRESERVEX>(
    schema = "asm_rawreservex#fb03 = RAWRESERVEX;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RAWRESERVEX) {
    }

    override fun loadTlb(cellSlice: CellSlice): RAWRESERVEX = RAWRESERVEX
}
