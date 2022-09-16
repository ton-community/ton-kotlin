package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SSKIPFIRST : AsmInstruction, TlbConstructorProvider<SSKIPFIRST> by SSKIPFIRSTTlbConstructor {
    override fun toString(): String = "SSKIPFIRST"
}

private object SSKIPFIRSTTlbConstructor : TlbConstructor<SSKIPFIRST>(
    schema = "asm_sskipfirst#d731 = SSKIPFIRST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SSKIPFIRST) {
    }

    override fun loadTlb(cellSlice: CellSlice): SSKIPFIRST = SSKIPFIRST
}
