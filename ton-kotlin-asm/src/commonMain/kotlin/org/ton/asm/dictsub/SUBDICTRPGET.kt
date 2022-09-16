package org.ton.asm.dictsub

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTRPGET : AsmInstruction, TlbConstructorProvider<SUBDICTRPGET> by SUBDICTRPGETTlbConstructor {
    override fun toString(): String = "SUBDICTRPGET"
}

private object SUBDICTRPGETTlbConstructor : TlbConstructor<SUBDICTRPGET>(
    schema = "asm_subdictrpget#f4b5 = SUBDICTRPGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTRPGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTRPGET = SUBDICTRPGET
}
