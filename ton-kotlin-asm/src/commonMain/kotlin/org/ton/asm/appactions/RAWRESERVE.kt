package org.ton.asm.appactions

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object RAWRESERVE : AsmInstruction, TlbConstructorProvider<RAWRESERVE> by RAWRESERVETlbConstructor {
    override fun toString(): String = "RAWRESERVE"
}

private object RAWRESERVETlbConstructor : TlbConstructor<RAWRESERVE>(
    schema = "asm_rawreserve#fb02 = RAWRESERVE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RAWRESERVE) {
    }

    override fun loadTlb(cellSlice: CellSlice): RAWRESERVE = RAWRESERVE
}
