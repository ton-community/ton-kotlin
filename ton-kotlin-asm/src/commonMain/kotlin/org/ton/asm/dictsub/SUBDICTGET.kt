package org.ton.asm.dictsub

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTGET : AsmInstruction, TlbConstructorProvider<SUBDICTGET> by SUBDICTGETTlbConstructor {
    override fun toString(): String = "SUBDICTGET"
}

private object SUBDICTGETTlbConstructor : TlbConstructor<SUBDICTGET>(
    schema = "asm_subdictget#f4b1 = SUBDICTGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTGET = SUBDICTGET
}
