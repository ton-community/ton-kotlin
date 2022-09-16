package org.ton.asm.dictsub

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUBDICTIGET : AsmInstruction, TlbConstructorProvider<SUBDICTIGET> by SUBDICTIGETTlbConstructor {
    override fun toString(): String = "SUBDICTIGET"
}

private object SUBDICTIGETTlbConstructor : TlbConstructor<SUBDICTIGET>(
    schema = "asm_subdictiget#f4b2 = SUBDICTIGET;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUBDICTIGET) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUBDICTIGET = SUBDICTIGET
}
